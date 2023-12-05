<p align="center"> 
  <a href="https://spring.io/" target="_blank">
    <img width="80%" src="https://logodownload.org/wp-content/uploads/2018/05/picpay-logo.png"/>
  </a> 
</p>

<h4 align="center" >🚀 🟩 Pic Pay API 🟩 🚀</h4>

<h4 align="center">
  My implementation of <a href="https://picpay.com/" target="_blank" >@PicPay</a> Technical Challenge using Java & Spring Framework to create a little representation of PicPay API
</h4>

<p align="center">
  |&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#project">Overview</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#techs">Technologies</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#app">Project</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#routes">Routes</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#run-project">Run</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
  <a style="color: #8a4af3;" href="#author">Author</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
</p>

#

<h1 align="center">
  
  <a href="https://github.com/Samuel-Ricardo">
    <img src="https://img.shields.io/static/v1?label=&message=Samuel%20Ricardo&color=black&style=for-the-badge&logo=GITHUB"/>
  </a>

  <a herf="https://www.instagram.com/samuel_ricardo.ex/">
    <img src='https://img.shields.io/static/v1?label=&message=Samuel.ex&color=black&style=for-the-badge&logo=instagram'/> 
  </a>

  <a herf='https://www.linkedin.com/in/samuel-ricardo/'>
    <img src='https://img.shields.io/static/v1?label=&message=Samuel%20Ricardo&color=black&style=for-the-badge&logo=LinkedIn'/> 
  </a>

</h1>

<br>

<p id="project"/>

<h2> | 🛰️ About:  </h2>

<p align="justfy">
  This API is my implementation of the PicPay Technical Challenge [<a href="https://github.com/PicPay/picpay-desafio-backend" > repository </a>]. It simulate a microsservice for transactions with authentication & authorization. Was made following the best pratice of Clean Architeture and DDD, with Tests, Docker Containers and full Documentation with Swagger.
</p>

<br>

📡 | Hosted on Render: https://pic-pay.onrender.com/ <br>
📝 | Documentation: https://pic-pay.onrender.com/swagger-ui/index.html

<br>
<br>

<h2 id="techs">
| 🏗️ - Technologies and Concepts Studied:
</h2>

> <a href='https://spring.io/'> <img width="40px" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" /> </a>

<br>

- Spring Framework
- Spring Security
- JWT
- Authentication & Authorization
- PostgreSQL
- Docker
- Consume External API
- Maven
- REST
- Swagger
- Validation
- Lombok
- Environment
- Clean Architeture
- DDD
- JUnit
- H2 Database
- Error Handler

> Among Others...
<br>


#

<h2 id="app">
  💻 | Application:
</h2>

<img src="https://files.tecnoblog.net/wp-content/uploads/2022/01/pp-edited.jpg"/>

<br>

<p>
  The API have a Authentication Systems that identify the registered user based on a JWT Token and a Authorization sistem that define what resources a authenticated usar can access, a <b>normal</b> user can't create others users, but a <b>admin</b> user can, for example. The User password are hashed with Bcrypt for better security.
</p>

<p>
  Once time authenticated, a normal user can transact a value with others if have sufficient balance, it ask to for a external mocked microsservice if can realize transaction. All Domain Rules are encpsulateds in Policies that are implemented in Application Layer. after transaction are effectived with success the API consult other external mocked microsservice to send a notification for users informating that all goes right.
<p>

<p>
  All data passed throug this microsservice have validation and a expected structure defined by DTO's and all Documentation are made with Swagger
</p>

<p>
  In Test Ambience, i setup H2 for Database instead a dedicated PostgreSQL, because H2 use Memory that is very fast and how it is just a test and i dont need to keep data, i really want that the data base reset every time it restart, in other words, is more fast and discartable while emulate a real database comportament, is perfect for tests.
</p>

<p>
  I use Docker to setup enviroment and run tests. This application have a image that are published on github at every commit on Main Branch automatizaded by Gihub actions
</p>


<br>

#

<h2 id="run-project"> 
   👨‍💻 | How to use
</h2>

<br>

### Open your Git Terminal and clone this repository

```git
  $ git clone "git@github.com:Samuel-Ricardo/Pic-Pay_simplified.git"
```

### Make Pull

```git
  $ git pull "git@github.com:Samuel-Ricardo/Pic-Pay_simplified.git"
```

<br>

This application use `Docker` so you dont need to install and cofigurate anything other than docker on your machine.

> <a target="_blank" href="https://www.docker.com/"> <img width="48px" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-plain-wordmark.svg" /> </a>

<br>

Navigate to project folder and run it using `docker-compose`

```bash

  # After setup docker environment just run this commmand on root project folder:

  $ docker-compose up --build   # For First Time run this command

  $ docker-compose up           # to run project


```

```bash

  #Apps Running on:

  $ API: http://localhost:8080

  $ PostgreSQL: http://localhost:5432
  $ PGAdmin: http://localhost:5050

  See more: ./docker-compose.yaml

```

<br>

#

<h2 id="author">
  :octocat: | Author:  
</h2>

> <a target="_blank" href="https://www.linkedin.com/in/samuel-ricardo/"> <img width="350px" src="https://github.com/Samuel-Ricardo/bolao-da-copa/blob/main/readme_files/IMG_20220904_220148_188.jpg?raw=true"/> <br> <p> <b> - Samuel Ricardo</b> </p></a>

<h1>
  <a herf='https://github.com/Samuel-Ricardo'>
    <img src='https://img.shields.io/static/v1?label=&message=Samuel%20Ricardo&color=black&style=for-the-badge&logo=GITHUB'> 
  </a>
  
  <a herf='https://www.instagram.com/samuel_ricardo.ex/'>
    <img src='https://img.shields.io/static/v1?label=&message=Samuel.ex&color=black&style=for-the-badge&logo=instagram'> 
  </a>
  
  <a herf='https://twitter.com/SamuelR84144340'>
    <img src='https://img.shields.io/static/v1?label=&message=Samuel%20Ricardo&color=black&style=for-the-badge&logo=twitter'> 
  </a>
  
   <a herf='https://www.linkedin.com/in/samuel-ricardo/'>
    <img src='https://img.shields.io/static/v1?label=&message=Samuel%20Ricardo&color=black&style=for-the-badge&logo=LinkedIn'> 
  </a>
</h1>


<br>

