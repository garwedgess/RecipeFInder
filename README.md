
# Recipes

[![Unit Tests](https://github.com/garwedgess/RecipeFinder/actions/workflows/unit_tests.yml/badge.svg)](https://github.com/garwedgess/RecipeFinder/actions/workflows/unit_tests.yml)

## Description
Get a list of ingredients from the [provided API](https://run.mocky.io/v3/45a5a07f-e981-4918-9c31-090b121d6c5f)    
Based on the provided ingredients, only recipes which contain the available ingredients should be displayed.


<p align="center">    
   <img align="center" width="20%" height="20%" src="https://raw.githubusercontent.com/garwedgess/RecipeFinder/master/app_demo.gif" alt="app-demo" border="0">
</p>    

## Project structure

This app uses a basic implementation of clean architecture.

- **data**    
  -- Api: Remote API to fetch the available ingredients using [Ktor](https://ktor.io/)
  -- Data Sources: Contains cache and remote data sources for the ingredients and cache data source for recipes
  -- Repository: Used to fetch ingredients from cache or network, for fetching recipes from cache and combining the data to be returned to the UI
  -- Mappers: used for mapping data layer models to domain entities
  -- Model - for all data classes
  -- Utils: contains an extension function on the HttpClient to wrap the API response
- **domain**
  -- Entities: which are mapped from data models using the Mappers in the data later
  -- Usecases: used for executing repository methods. These use cases are used by the ViewModels 
- **di**    
  -- Contains the Hilt modules used for dependency injection
- **ui**    
  -- Contains the ViewModels and the components used to build the UI using Jetpack Compose

## Features

- **Home screen**: Displays a list of ingredients and recipes that are compatible with the available ingredients. Ingredients can be removed which will update the compatible ingredients. This screen contains a SwipeRefreshLayout to refresh the available ingredients by fetching them from the API.
- **Details screen**: Displays the selected recipe and the ingredients required for this recipe. 

## Improvements

- Handle internet connectivity changes
- cache the results in a local database, removing the cache data sources and replacing with local data sources


## Testing

The app contains a number of unit tests which have been integrated with Github actions. The unit tests can be run as a workflow [here](https://github.com/garwedgess/RecipeFinder/actions/workflows/unit_tests.yml):

The app contains a number of integration tests to test the different UI states for the **HomeScreen** and the **DetailsScreen**
