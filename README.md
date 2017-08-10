# Smart_Commuter
Copyright © 2017 Ulysses Meza and Jinghui Han. <br />
This app is under the "MIT License". Please see the file LICENSE in this distribution for license terms.

**Contributor**: Ulysses Meza(umeza@pdx.edu) AND Jinghui Han (jinghan@pdx.edu) <br />
(Feel free to contact us for anything[like bugs, contributing new features, etc] about this project)

## Week 3 Report
### What we have been working on
We have been learning about android/mobile development, since no one on our team has previous experience with it and we have decided
on using Android Studio (a Visual studio tool for android development) we have been experimenting with making simple apps to get an
understanding of what is required for the UI and the backend to work. We also spent time designing the project.
### Challenges
The main challenges we have run into have been with setting up Visual Studio and just not having experience with android development.
### Moving forward
This week and next week we will focus on getting the backend started (first getting a small POC done at the very least) with the
logic that we will need.

## Project Description
This is a mobile app that can be used to find public/ride share transportation in the metro area. We know that there are a lot of good transportation apps out there, but some have too much features which would maybe confuse users. Hence, our general idea behind this app is to provide user a friendly and easy use app with only needed features, so allowing users to easily meet their needs upon using public transportatoin in the metro area. <br />


## Project Structure
The app is splited with few activities
* Trimet StopId Arrival Time LookUp 
* Price lookup through Lyft and Uber
* Check nearby stops upon a current location coordinate

## Build Instructions
This app is written with JAVA and XML using Android Studio.
* Build Prerequisites: Android Studio
* Build: Download needed packages when you are in the studio and Run the program
* Minimum SDK: Version 23  
* Platform: Android only; Language: en-US       
* Download: git clone https://github.com/mezau532/smart_commuter.git  OR Download the zip file from this site.
* Open the project and go to app->java->config->configFile.java and set all the api keys, you will need a google, uber, lyft, and trimet api key
* Note: In computer, you need to send your location coordinate first before using the nearbylocation for the trimet and current_location in the uber/lyfy service

## User Manual
How to use the features: 
* StopID lookup: enter StopId and Click Search Button to see arrival time for the stop.
* Find Ride: Enter address(OR check the current location checkbox) to check price and duration to your destination.
* Nearby Stops lookup: Click the check box then click search, it will show you stops(if has) within 0.5 miles in you current location.
* Maybe: (thinking to add: Real-time tracking GPS for your trimet trip)

## References
* The HttpHandler class is based on TutorialsPoint: https://www.tutorialspoint.com/android/android_json_parser.html
* Both Trimet services JSON parser are based on: https://www.androidhive.info/2012/01/android-json-parsing-tutorial by RAVI TAMADA
* GetLocationClass is based on: https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial by RAVI TAMADA
