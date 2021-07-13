# Party-DRD

## Use case
The Party DRD calculates in its top level decision how many bottles of each beverage should be bought.
For this we need to know how many bottles we already have in our cellar and how many we need at all. The latter aspect
depends on the number of guests, the chosen dish (depending on season) and, of course, the gender of the guests. (Currently and politically incorrect but for the sake of simplicity sufficient we only use male and female).

## How to execute
That decision can be called via REST by providing the following parameters (it will not work in the DMN-Simulator as the guests input is a list of guests):

http://localhost:8080/engine-rest/decision-definition/key/beveragesToBuy/evaluate

    {
      "variables": {
          "guests": {
              "value": [
                  {
                      "age": 41,
                      "gender": "male",
                      "vegetarian": false
                  },
                  {
                      "age": 39,
                      "gender": "female",
                      "vegetarian": false
                  },
                  {
                      "age": 9,
                      "gender": "male",
                      "vegetarian": false
                  },
                  {
                      "age": 5,
                      "gender": "female",
                      "vegetarian": false
                  }
              ]
          },
          "season": {
              "value": "Summer"
          },
          "availableBeer": {
              "value": 0
          },
          "availableLemonade": {
              "value": 0
          },
          "availableWine": {
              "value": 0
          },
          "availableHefeweizen": {
              "value": 0
          },
          "availablePinotGrigio": {
              "value": 0
          }
      }
    }
