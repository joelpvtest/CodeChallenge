# language: en
@CucumberReady
Feature: Gather data in regards to medical-related headlines.
Must pull the headline, author, link to article, date/time posted and any included images related to that article.

Background: We need to open the browser in the required Page (URL).

Given I Open the browser
Then I Validate that Page Title is "Associated Press News"
When I Close popup window if it appears
Then I Validate that the page at least displayed the item "main.logo"


Scenario: Validate capability to displayed data in regards to medical-related headlines.
Given I Gather data each page result that regards to "medic"
Then I Close the browser
