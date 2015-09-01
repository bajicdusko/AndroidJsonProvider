What you can do with this library:
<ul>
	<li><strong>Get your model class instance only with predefined URL</strong></li>
	<li><strong>Custom model class attributes mapping </strong</li>
	<li><strong>In depth model class recognition</strong></li>
	<li><strong>Asynchronous usage </strong></li>
	<li><strong>Content caching</strong></li>
	<li><strong>Default error dialog</strong></li>
</ul>



===== <span><u>Examples</u></span> =====

**Example 1 - Simple single model class** -
First thing you need to do is to create your model class. Lets create simple Planet class with only one attribute.<span><u> All model classes needs to extend base Model class.</u></span>

    public class PlanetModel extends Model {
 
        @JsonResponseParam(Name = "name")
        public String Name;
    }

Below is JSON content that corresponds to Planet class.

 Planet.json
 
    {
 	    "name": "Mercury"
    }

AJP call is shown below.

    public class MainActivity extends ActionBarActivity {
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
 
         new AsyncJsonProvider<PlanetModel, Model>(this, PlanetModel.class, "http://url").shortExecute(new OnDataLoaded() {
             @Override
             public void OnModelLoaded(PlanetModel responseModel) {
                 //this is where you put all after data loading logic
             }
 
             @Override
             public void OnErrorOccured(String message) {
                 //if AJP is loaded with Context instance this is where you have to put error management logic
             }
         });
     }
 }

Network call and content parsing are invisible and done in background. If everything is ok, model class instance is returned in ''OnModelLoaded'' method. This method should contain all "after data loading" logic (loading animation hiding, views initializations and so on..) As i mentioned, AJP can be instantiated with FragmentActivity instance or with Context instance. In given example, AJP is instantiated with ActionBarActivity (which is FragmentActivity subclass), and if any error occur, MessageDialog will pop up.


**Example 2 - Model classes nesting** - In this example our model class contains array of another model class. There are no nesting levels limitations.

    public class Sattelite extends Model {
 
        @JsonResponseParam(Name = "satteliteName")
        public String Name;
    }
 
    public class ExtendedPlanetModel extends Model {
 
        @JsonResponseParam(Name = "name")
        public String Name;
 
        @JsonResponseParam(Name = "sattelites")
        public Sattelite[] Sattelites
    }

JSON content that corresponds to ExtendedPlanet class.

 ExtendedPlanet.json
 
    {
       "name": "Jupiter",
       "sattelites": [{
        "satteliteName": "Europa"
 	      },{
 		      "satteliteName": "Ganymede"
 	      }]
    }

AJP call is identical in both cases with difference of model class usage (Planet vs ExtendedPlanet). 


**Example 3 - Model array** - JSON content that corresponds to PlanetModel array class.
 PlanetArray.json
 
    [{
 	    "name": "Mercury"
    },{
 	    "name": "Venus"
    },{
 	    "name": "Earth"
    },{
 	    "name": "Mars"
    },{
 	    "name": "Jupiter"
    },{
 	    "name": "Saturn"
    },{
 	    "name": "Uranus"
    },{
 	    "name": "Neptune"
    }]

AJP call.

      new AsyncJsonProvider<PlanetModel, Model>(this, PlanetModel.class, "http://url").shortArrayExecute(new OnDataLoaded() {
             @Override
             public void OnModelArrayLoaded(PlanetModel[] responseArrayModel) {
                 //todo something with planet array data
             }
 
             @Override
             public void OnErrorOccured(String message) {
                 //todo something is AJP is instantiated with Context
             }
         });

You'll notice difference in AJP call for single model and array content.(''OnModelLoaded'' vs ''OnModelArrayLoaded'')
