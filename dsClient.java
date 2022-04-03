//Francesca Nicholson
//45903824




//Java import packages for network, I/O, ArrayLists, XML parsers(DOM) & exceptions
import java.net.*;
import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


class dsClient {

    public static void main (String args[]) {
        //used for authentication of username 
        String authName = System.getProperty("user.name");
        Socket s = null;
        String response = "";

        try{
            //server port used for ds-client as specified in marking criteria
            int serverPort = 50000;
            s = new Socket("localhost", serverPort);
            //reader and output streams for I/O communications of ds-client and ds-server
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream( s.getOutputStream());

            //handshaking with HELO to ds-server
            out.write(("HELO\n").getBytes()); 
            //gathering response from ds-server
            response = in.readLine();
            //printing out response from ds-server
            System.out.println(serverResponse(response));

            //authenticating username with ds-server
            out.write(("AUTH " + authName + "\n").getBytes());
            //gathering response from ds-server
            response = in.readLine();
            //printing out response from ds-server
            System.out.println(serverResponse(response));

            
            //starting the loop to go through every server/job and I/O
            //while(!response.equals("NONE")){

            //telling ds-server that ds-client is ready for starting job information/scheduling
            out.write(("REDY\n").getBytes());
            //gathering response from ds-server
            response = in.readLine();
            //printing out response from ds-server
            System.out.println(serverResponse(response));

            //used for spliting server response
            String[] jobDetails = response.split(" ");
            int detailsLength = jobDetails.length;

            //requesting server info from ds-server 
            out.write(("GETS Capable "+ jobDetails[detailsLength - 3] + " " + jobDetails[detailsLength - 2] + " " + jobDetails[detailsLength - 1] +"\n").getBytes());
                
            //gathering response from ds-server
            response = in.readLine();
                   
            //splits the server data into chunks of info (as seperated by " ")
            String[] serverData = response.split(" ");

            //for each server, send OK, and read response    
            for (int i = 0; i < Integer.valueOf(serverData[1]); i++) {
                out.write("OK\n".getBytes());
                response = in.readLine();
                System.out.println(serverResponse(response));
                
            }  

            //printing out response from ds-server
            System.out.println(serverResponse(response)); 

            //used to proceed to end
            out.write("OK\n".getBytes());
            //gathering response from ds-server
            response = in.readLine();
            //quits simulation
            out.write(("QUIT\n").getBytes());
            out.flush();
            out.close();
            s.close();
            
        
        }
//error catching
    catch (UnknownHostException e){
        //used to catch unknown socket host exceptions
        System.out.println("Sock:"+e.getMessage());
    }

    catch (EOFException e){
        //used to catch end of file exceptions
        System.out.println("EOF:"+e.getMessage());
    }
    catch (IOException e){
        //used to catch input/output exceptions
        System.out.println("IO:"+e.getMessage());
        } 
        //trying to close the system if the socket is null
    if(s!=null) try {
        s.close();
        }
        catch (IOException e){
            //used to catch input/output exceptions
            System.out.println("close:"+e.getMessage());
        }
    }

    //used to see server response
    public static String serverResponse(String serverResponse) {
        return "RCVD: " + serverResponse;
    }
   
}

//class that helps to populate the Server ArrayList
class Server{
    public String type;
    public int id;
    public int cores;
    public int memory;
    public int disk;

   //returns all of the items in one 'line'
   public String toString(){
       return "Server [type = " + type + "id = " + id + ", cores = " + cores + ", memory = " + memory + ", disk = " + disk + "]";
   }   
 
   //gets the type of server and returns it
   public String getType(){
       return type;
   }
 
   //sets the current type of server as the returned type from getType()
   public void setType(String type){
       this.type = type;
   }
 
 
   //gets the core info of server and returns it
   public int getCores(){
       return cores;
   }
 
   //sets the current core info of server as the returned core info from getCores()
   public void setCores(int cores){
       this.cores = cores;
   }
 
   //gets the memory info of server and returns it
   public int getMemory(){
       return memory;
   }
 
   //sets the current memory of server as the returned memory info from getMemory()
   public void setMemory(int memory){
       this.memory = memory;
   }
 
   //gets the disk info of server and returns it
   public int getDisk(){
       return disk;
   }
 
   //sets the current disk info of server as the returned disk info from getDisk()
   public void setDisk(int disk){
       this.disk = disk;
   }

}
 
class xmlReader{
// DOM READER FOR XML FILE
 
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
       //initialises the subclass to run
       List<Server> servers = parseConfigXML();
   }
 
 
   public static List<Server> parseConfigXML() throws ParserConfigurationException, SAXException, IOException{
       //making a new ArrayList to store the XML information
       List<Server> servers = new ArrayList<Server>();
       Server server = null;
       //gets document builder - api that allows parsing to happen
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       //builds document
       DocumentBuilder builder = factory.newDocumentBuilder();
       //parses the XML file
       Document document = builder.parse(new File("/home/francesca/ds-sim/src/pre-compiled/../"));
       //normalises the file (reducing redundancies)
       document.getDocumentElement().normalize();
 
       int serverSize = 0;
 
       //makes new nodelist for storing the servers & their attributes
       NodeList serverList = document.getElementsByTagName("server");
       for (int temp = 0; temp < serverList.getLength(); temp++){
           Node node = serverList.item(temp);
           if (node.getNodeType() == Node.ELEMENT_NODE){
               Element serverElement = (Element) node;
               //creates new Server object
               server = new Server();
               //uses the methods from Server class to gather attribute info/values
               server.setType(serverElement.getElementsByTagName("type").item(0).getTextContent());
               server.setLimit(Integer.parseInt(serverElement.getAttribute("limit")));
               server.setBootupTime(Integer.parseInt(serverElement.getAttribute("bootupTime")));
               server.setHourly(Double.parseDouble(serverElement.getAttribute("hourlyRate")));
               server.setCores(Integer.parseInt(serverElement.getAttribute("cores")));
               server.setMemory(Integer.parseInt(serverElement.getAttribute("memory")));
               server.setDisk(Integer.parseInt(serverElement.getAttribute("disk")));
 
               //adds current server to list
               servers.add(server);
               serverSize++;
           }
       }
       //returns server info
       return servers;
   }
}
