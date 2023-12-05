<p align="center"> 
  <a href="https://spring.io/" target="_blank">
    <img width="80%" src="https://logodownload.org/wp-content/uploads/2018/05/picpay-logo.png"/>
  </a> 
</p>

<h4 align="center" >🚀 🟩 Pic Pay API 🟩 🚀</h4>

<h4 align="center">
  My implementation of <a href="https://picpay.com/" target="_blank" >@PicPay</a> Technical Challenge using Java and Spring Framework to create a small representation of the PicPay API
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
  This API is my implementation of the PicPay Technical Challenge [<a href="https://github.com/PicPay/picpay-desafio-backend" > repository </a>]. Simulates a microservice for transactions with authentication and authorization. It was done following the best practices of Clean Architecture and DDD, with Tests, Docker Containers and complete Documentation with Swagger.
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
 The API has an Authentication System that identifies the registered user based on a JWT Token and an Authorization System that defines which resources an authenticated user can access, a <b>normal</b> user cannot create other users, but a <b>admin</b> user can, for example. User password is encrypt with Bcrypt for better security.
</p>

<p>
  Once authenticated, a normal user can transact an amount with others if they have enough balance, they will request an external simulated microservice to see if they can carry out the transaction. All Domain Rules are encapsulated in Policies that are implemented at the Application Layer. After the transaction is completed successfully, the API consults another external simulated microservice to send a notification to users informing them that everything went well.
<p>

<p>
  All data passed through this microservice has validation and expected structure defined by DTOs and all documentation is done with Swagger
</p>

<p>
  In the testing environment, I have configured H2 for Database instead of a dedicated PostgreSQL as H2 uses Memory which is very fast and since it is just a test and I don't need to save data, I actually want the database to be restarted when starting, that is, it is faster, disposable and emulates the real behavior of a database, it is perfect for testing.
</p>

<p>
  I use Docker to set up the environment and run tests. This application has an image that is published on github with each commit in the Main Branch automated by Github actions
</p>

<br>

📡 | Hosted on Render: https://pic-pay.onrender.com/ <br>
📝 | Documentation: https://pic-pay.onrender.com/swagger-ui/index.html

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

