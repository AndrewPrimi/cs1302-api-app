# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

This app allows users to input the callsign of a commercial airplane and receive detailed information about its origin and destination airports. It provides an intuitive way for aviation enthusiasts, travelers, and developers to learn where an aircraft is coming from and where it’s going using real-time and structured airport data.

The app connects two external APIs in a meaningful and automated way:

ADSBdb API – When a user enters an aircraft callsign (e.g., “DLH123”), the app queries the ADSBdb API to retrieve key flight details, specifically the ICAO codes for the origin and destination airports of the flight.

AirportsAPI – Once the ICAO codes are obtained, the app then uses them to query the AirportsAPI. This returns information such as the airports website url, the name, icao code etc. Some of the same information can be found with the ADSBD API however most is not.

By integrating these two APIs, the app enables users to seamlessly go from a basic callsign to full airport context in just one search. It automates the process of linking flight tracking data with airport data, making it both practical and informative. One downside is that the airportApi has a very limited database of airports meaning that the user will mosty likely get erros messages because the api itself does not have information about the specfic aircarft's destination and origin airport. The api itself has a message on the website stating that most aiports are located in Germany.
Here is a list of inputs to use in the program: CFG1TL DLH66 DLH400 OCN245 TUI678. These give results because they are German airlines.

https://github.com/AndrewPrimi/cs1302-api-app.git

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://api.adsbdb.com/v0/callsign/OCN245

```

> Replace this line with notes (if needed) or remove it (if not needed).

### API 2

```
https://airport-web.appspot.com/_ah/api/airportsapi/v1/airports/EDDL
```

> Replace this line with notes (if needed) or remove it (if not needed).

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

One of the most exciting things I learned from working on this project was how to integrate multiple external APIs in a meaningful and automated way. Before this, I had only worked with single API endpoints, but connecting the ADSBdb API with the AirportsAPI taught me how to chain API responses—using data from one request as input for another. This experience helped me understand real-world data flow, error handling between services, and how to transform raw flight data into useful information for users. It felt like building a small, functional ecosystem of live aviation data.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

Initally I would pick an api other than airportApi becuase it is super limited with the number of airports it has information for. It says on the website most airports they have infomation for are from Germany however, this made it super difficult to test and reduces the usability of the app.If I were to restart this project, I would approach the API integration with a more modular design from the beginning. Initially, I tightly coupled the logic for calling and parsing both the ADSBdb and AirportsAPI into single blocks of code. While functional, it made debugging and expanding the app harder as it grew. In hindsight, creating separate, reusable classes or methods for each API interaction would have improved the clarity, testability, and scalability of the code. This experience showed me the value of clean separation of concerns and how much easier it is to maintain and extend code when components are loosely coupled and well-structured.
