<h3>To install this service do the following:</h3>
<h4>Preconditions</h4>
I assume you have installed tomcat9, maven.

<h4>Procedure</h4>
Go to the project directory, and build maven package.<br>
<code>mvn package</code><br>
after that copy the ./target/booking.war file to tomcat webapps<br>

start the tomcat server and go to the DEMO SITE http://localhost:8080/booking<br>

<h4>Installation script on manjaro</h4>
cd ~<br>
git clone https://github.com/CodeServant/bookingAppp.git <br>
cd bookingAppp<br>
mvn package<br>
sudo cp ./target/booking.war /usr/share/tomcat9/webapps/<br>
systemctl start tomcat9<br>
# opens the DEMO APP in a browser<br>
xdg-open http://localhost:8080/booking/<br>
<h3>Scripts</h3>

# show all screenings between 2022-10-13 and 2050-10-10 <br>
curl 'http://localhost:8080/booking/movies?from=2022-10-13%2010:30&to=2050-10-10%2010:30'<br>

# show screening (id=5) with already reserved places <br>
curl 'http://localhost:8080/booking/screenings?id=5' <br>

# reserving places for the screening <br>
curl -i -X POST 'http://localhost:8080/booking/reservation' -d 'name=Gracjanna&surname=Kowalska-Malinowska&screening=1&r=0&p=0&r=0&p=1'