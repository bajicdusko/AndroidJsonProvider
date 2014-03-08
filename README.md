Android Json Provider - AJP
===================

Library mostly used for generic parsing(serializing) of JSON responses/requests, with useful android utils. Similar to existing GSON library, AJP uses Java generics with use of @JsonResponseParam and @JsonRequestParam to map class attributes with attributes in JSONObject. It is intended for use in android projects, but with few modifications (removing Context arguments) it can be used in all Java projects.

This is mostly usefull if JSON web service is made in local language, and you (i hope) develop in english, so attribute names of your class does not have to be identical as attribues of JSONObject.

Eg: 
- JSON:
```
{
    "ImePlanete": "Jupiter",
    "Sateliti": [{
        "Ime": "Europa"
    }, {
        "Ime": "Ganymede"
    },{
        "Ime": "Calisto"
    }]
}
```

- Your class:
```
class PlanetModel extends Model
{
    @JsonResponseParam(Name="ImePlanete")
    public String PlanetName;

    @JsonResponseParam(Name="Sateliti")
    public SatelliteModel[] Satellites;
}

class SatelliteModel extends Model
{
    @JsonResponseParam(Name="Ime")
    public String Name;
}
```


Yes, library recognizes arrays in JSON file if you have defined array in your class. All your model classes <b>needs to extend Model class</b> which contains most of business logic for json parsing.

Lets use classes defined above and JSON defined above. To fetch data from JSON and serialize it to defined model class, you have to make this call:

```
PlanetModel planet = Provider.getModel(new PlanetModel(), context, planetJsonUrl);
```

(if you have only array in your JSON file, you can use method call defined below)
```
ArrayModel planet = Provider.getArrayModel(new ArrayModel(), context, planetJsonUrl);
```

And that's it. Lines above throws few exceptions, which you can catch in your activity/fragment and to show your user exact error message. Provider class checks if connection exists, or it checks type of network connection (WIFI/Mobile), and few other useful exceptions.

Provider class contains pure JSONObject and String representation of JSONObject, which you can use for any additional actions unsupported by this library.


If you have noticed, besides @JsonResponseParam there is @JsonRequestparam annotation. It can be used in for posting arguments to webservice, for wich you expect response.

Eg:

- CLASS
```
public class Login extends Model
{
    @JsonRequestParam(Name="KorisnickoIme")
    public String Username;
    
    @JsonResponseParam(Name="Kljuc")
    public String AuthKey;
}

public class User extends Model
{
    @JsonResponseParam(Name="Ime")
    public String Name;
    
    @JsonResponseParam(Name="Prezime")
    public String Surname;
}
```

- METHOD CALL
```
Login login = new Login();
login.Username = "myUsername";
login.AuthKey = "myAuthKey";

User user = Provider.getModel(new User(), context, loginUrl, login);
```

Method call above, creates HTTP POST request with request params: "KorisnikoIme=myUsername" and "Kljuc=myAuthKey". It is expected that loginUrl returns JSON in this format:
```
{
  "Ime": "exampleName",
  "Prezime": "exampleSurname"
}
```

