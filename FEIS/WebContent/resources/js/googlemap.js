/**
 * Google Map API interaction
 */
var map;
var marker;
var markersArray = [];
function initialize() {
	  var myLatlng = new google.maps.LatLng(46.8722222,-113.9930556);
	  var myOptions = {
	    zoom: 9,
	    center: myLatlng,
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  }
	  map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
	  google.maps.event.addListener(map, 'click', function(event) {
	    placeMarker(event.latLng);
	  });
	 
	 
	}
function placeMarker(location) {
		marker = new google.maps.Marker({
		  position: location, 
		  title: location.toString(),
		  clickable: true,
		  map: map
		});

		markersArray.push(marker);
		google.maps.event.addListener(marker,"click",function(event){
			showCoordinate(location);
		});
		 google.maps.event.addListener(marker, 'rightclick', function (event) {
	         removeCoordinate();
	    });
		
		
}
function showCoordinate(location) {
	
	   var longitude = Math.abs(location.lng());
	   longitude = longitude.toFixed(6);
	   
	   var latitude = Math.abs(location.lat());
	   latitude = latitude.toFixed(6);
	
	   //var coordinates = marker.getPoint();
	   document.getElementById('mlatitude').value = latitude;
	   document.getElementById('mlongitude').value = longitude;
	   alert("long:" + longitude + "lat:" + latitude);

}

function removeCoordinate() {
	
       if (markersArray) {
    	   for (i in markersArray) {
    		   markersArray[i].setMap(null);
    	   }
       }
}



 