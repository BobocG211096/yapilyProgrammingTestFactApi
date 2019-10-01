# Introduction

This service handles the facts. It's meant to receive the request from 
the people who want to have a piece of a random interesting fact.

## Fact Controller

Is created for responding all the requests from clients to send back the 
cached fact response retrieved from Random Useless Fact API. It also can
retrieve the fact by id and additionally by the use of Yandex API can translate
the text to a different API using the ISO 639-1 language code.

## Status Fact Controller

Is created for responding the requests by sending a response in terms of the 
state of the server in the moment of caching data from the Random Useless Fact
API.