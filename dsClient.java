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
            String data = in.readLine();
            //printing out response from ds-server
            System.out.println("Received: "+ data);

            //authenticating username with ds-server
            out.write(("AUTH " + authName + "\n").getBytes());
            //gathering response from ds-server
            String auth = in.readLine();
            //printing out response from ds-server
            System.out.println("Received: "+ auth);

            //telling ds-server that ds-client is ready for starting job information/scheduling
            out.write(("REDY\n").getBytes());
            //gathering response from ds-server
            String readData = in.readLine();
            //printing out response from ds-server
            System.out.println("Received: "+ readData);

            //requesting for ds-server to send all server state information
            out.write(("GETS All\n").getBytes());
            //gathering response from ds-server
            String readGets = in.readLine();
            //printing out response from ds-server
            System.out.println("Received: "+ readGets);

            //requesting ds-server for more information on the server
            out.write(("OK\n").getBytes());


            String readOK = in.readLine();
        
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
   
}
//class that helps to populate the Server ArrayList as seen in 'parseConfigXML()' below
class Server{
    //defining the attributes as seen in the XML config file
    String type;
    int limit;
    int bootupTime;
    double hourlyRate;
    int cores;
    int memory;
    int disk;

    //returns all of the items in one 'line'
    public String toString(){
        return "Server [type = " + type + ", limit = " + limit + ", bootup time = " + bootupTime + ", hourly rate = " + hourlyRate + ", cores = " + cores + ", memory = " +memory + ", disk = " + disk + "]";
    }    

    //gets the type of server and returns it
    public String getType(){
        return type;
    }

    //sets the current type of server as the returned type from getType()
    public void setType(String type){
        this.type = type;
    }

    //gets the limit of server and returns it
    public int getLimit(){
        return limit;
    }

    //sets the current limit of server as the returned limit from getLimit()
    public void setLimit(int limit){
        this.limit = limit;
    }

    //gets the bootup time of server and returns it
    public int getBootupTime(){
        return bootupTime;
    }
    //sets the current bootup time of server as the returned bootup time from getBootupTime()
    public void setBootupTime(int bootupTime){
        this.bootupTime = bootupTime;
    }

    //gets the hourly rate of server and returns it
    public Double getHourly(){
        return hourlyRate;
    }

    //sets the current hourly rate of server as the returned hourly rate from getHourly()
    public void setHourly(Double hourlyRate){
        this.hourlyRate = hourlyRate;
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

/*
class serverSorter{
    public static void main(String[] args)
        
}
*/

class xmlReader{
 // DOM READER FOR XML FILE

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
        //initialises the subclass to run
        List<Server> servers = parseConfigXML();
     /*   for (int temp = 0; temp < servers.getLength(); temp++){
            //compare based on cores
              

        }
*/

    }
   
   
    private static List<Server> parseConfigXML() throws ParserConfigurationException, SAXException, IOException{
        //making a new ArrayList to store the XML information
        List<Server> servers = new ArrayList<Server>();
        Server server = null;
        //gets document builder - api that allows parsing to happen
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //builds document
        DocumentBuilder builder = factory.newDocumentBuilder();
        //parses the XML file
        Document document = builder.parse(new File("/home/francesca/ds-sim/src/pre-compiled/"));
        //normalises the file (reducing redundancies)
        document.getDocumentElement().normalize();
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
            }
        }
        //returns server info
        return servers;
    }
}
