# jaankari
An Android app that makes Information and entermainment accessible to phones in remote areas with low connectivity.

## Screenshots

<a href="url"><img src="http://i.imgur.com/9A6ORmA.png"  height="400" width="230" ></a>
<br>

## Overview

Jaankari lets you browse several categories of information such as **News, Videos, Weather, Commodity Prices, Health facts** and **Educational books**. It intelligently downloads the relevant content for a user and provides a rich **offline experience**. The omni search lets users search for all the information available on the servers even when they don't have the files or are offline. Under the hood, it forms a **P2P network** over Wifi hotspot to let users fetch content from each other.

## Architecture

The project has three major components:
  1. **Recommendation System:**
    *Built with the Apache Mahout Machine Learning library.*

    Two different recommender systems were built each for Videos and Arcticles. 
    For Videos, rating on a scale of 5 was asked from user once he has viewed the video. 
    For articles, number of articles viewed by the user was used to compute the extent to which a user likes some article. 

  For experiencing more hits during searching, 2 step user-based **collaborative filtering** was used. 
  First, users interest for a particular category is computed by computing the interest of user for a particular category. 
  More articles should be recommended for the category user in more interested in. 
  Once the number of articles to be downloaded for a category is decided, using the user based similarity, most relavant articles/videos using the data of other users who have rated the items is recommended.

  2. **Backend services on the server:** *Built on NodeJS and MySQL*
  
    The data is crawled from multiple sources such as **NDTV** for News, **Youtube** for Videos, **Facts for Life** for Health articles, **Open Weatherr** for weather information, **agmarknet.nic.in** for Commodity prices.

  3. **The Android app** 
  
    a. **User Interface:** The homescreen UI is a slick tiled menu of different categories. 
It also includes a search bar that searches across the global database for News and articles. 
For browsing articles, we use a cool flip animation. 
Each activity such as Videos, News etc. also has a Navigation Drawer assosiated with it to browse Popular, Recommended and History. 
Weather information is displayed dynamically on the weather tile.

    b. **Wifi P2P Network:** We use a P2P network over Wifi hotspot to let users fetch content from each other. We preferred Wifi hostops over Bluetooth as they are **faster**, **consistent** and provide **multiple connections.**
  
