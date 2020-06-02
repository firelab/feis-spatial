# FEIS

There are 2 build profiles - fs and mtdc. The only thing the profiles do at this point is change the location of the 
included HTML files based on the different environments. 

For development we setup a k8s postgis database that developers could connect to using a port forward like so

<code> 
  kubectl port-forward  deployment/feis-postgis 5432:5432
</code>
<br/>
<br/>

which allows the developer machine to talk to the container db by their local port 5432

we could have setup a permanent service with a dedicated port as well but the project was not expected to last very long. 
