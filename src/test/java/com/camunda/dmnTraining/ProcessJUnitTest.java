package com.camunda.dmnTraining;

import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.camunda.feel.FeelEngine;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import camundajar.impl.scala.util.Either;

public class ProcessJUnitTest {

	@Rule
	public final ProcessEngineRule rule = new StandaloneInMemoryTestConfiguration().rule();

	@Before
	public void setup() {

		init(rule.getProcessEngine());
	}

	@Test
	public void testFeelExpression() {
		final FeelEngine engine = new FeelEngine.Builder()
//                .valueMapper(SpiServiceLoader.loadValueMapper())
//                .functionProvider(SpiServiceLoader.loadFunctionProvider())
				.build();

		final Map<String, Object> variables = withVariables(//
				"installmentRate", 200//
				, "borrowersIncome", 1200.00 //
				, "expenses", 800.00//
		);
		final Either<FeelEngine.Failure, Object> result = engine
				.evalExpression("installmentRate / (borrowersIncome - expenses) * 100", variables);

		if (result.isRight()) {
			final Object value = result.right().get();
			System.out.println("result is " + value);
			org.junit.Assert.assertEquals(50.0, Double.valueOf(value.toString()).doubleValue(), 0.0);
		} else {
			final FeelEngine.Failure failure = result.left().get();
			fail(failure.message());
		}
	}

	@Test
	@Deployment(resources = { "SingleEntryExample.dmn" })
	public void testSingleEntryDmn() {
		DmnDecisionResult decisionResult = processEngine()//
				.getDecisionService()//
				.evaluateDecisionByKey("SingleEntryExample")//
				.variables(withVariables("input1", "A", "input2", "A"))//
				.evaluate();

		String singleEntry = (String) decisionResult.getSingleEntry();
		assertEquals("A", singleEntry);

	}

	@Test
	@Deployment(resources = { "SingleResultExample.dmn" })
	public void testSingleResultDmn() {
		DmnDecisionResult decisionResult = processEngine()//
				.getDecisionService()//
				.evaluateDecisionByKey("SingleResultExample")//
				.variables(withVariables("input1", "A", "input2", "A"))//
				.evaluate();

		DmnDecisionResultEntries singleResult = decisionResult.getSingleResult();

		assert (singleResult.containsKey("output1"));
		assert (singleResult.containsKey("output2"));

		assertEquals(singleResult.get("output1"), "A");
		assertEquals(singleResult.get("output2"), 1);

	}

	@Test
	@Deployment(resources = { "CollectedEntriesExample.dmn" })
	public void testCollectedEntriesDmn() {
		DmnDecisionResult decisionResult = processEngine()//
				.getDecisionService()//
				.evaluateDecisionByKey("CollectedEntriesExample")//
				.variables(withVariables("input1", "A", "input2", "A"))//
				.evaluate();

		assertEquals(2, decisionResult.size());

		List<Object> collectEntries = decisionResult.collectEntries("output1");
		assertEquals("A", collectEntries.get(0));
		assertEquals("Z", collectEntries.get(1));
	}

	@Test
	@Deployment(resources = { "ResultListExample.dmn" })
	public void testResultListDmn() {
		DmnDecisionResult decisionResult = processEngine()//
				.getDecisionService()//
				.evaluateDecisionByKey("ResultListExample")//
				.variables(withVariables("input1", "A", "input2", "A"))//
				.evaluate();

		List<Map<String, Object>> resultList = decisionResult.getResultList();

		assertEquals(2, resultList.size());

		assertEquals("A", resultList.get(0).get("output1"));
		assertEquals(1, resultList.get(0).get("output2"));

		assertEquals("Z", resultList.get(1).get("output1"));
		assertEquals(26, resultList.get(1).get("output2"));
	}

}