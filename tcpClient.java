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


class tcpClient {

    public static void main (String args[]) {
    // arguments supply message and hostname of destination
    Socket s = null;
    try{
        int serverPort = 50000;
        s = new Socket("localhost", serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        DataOutputStream out = new DataOutputStream( s.getOutputStream());

        out.write(("HELO\n").getBytes()); // UTF is a string encoding see Sn 4.3
        String data = in.readLine();
        System.out.println("Received: "+ data);

        out.write(("AUTH francesca\n").getBytes()); // UTF is a string encoding see Sn 4.3
        String auth = in.readLine();
        System.out.println("Received: "+ auth);

        out.write(("REDY\n").getBytes());
        String readData = in.readLine();
        System.out.println("Received: "+ readData);

        
        out.write(("GETS All\n").getBytes());
        String readGets = in.readLine();
        System.out.println("Received: "+ readGets);

        out.write(("OK\n").getBytes());


        String readOK = in.readLine();
        
    }

    catch (UnknownHostException e){

        System.out.println("Sock:"+e.getMessage());
    }
    catch (EOFException e){
        System.out.println("EOF:"+e.getMessage());
    }
    catch (IOException e){
        System.out.println("IO:"+e.getMessage());
        } 
    if(s!=null) try {
        s.close();
        }
        catch (IOException e){
            System.out.println("close:"+e.getMessage());
        }
    }
   
}

class Server{
    String type;
    int limit;
    int bootupTime;
    double hourlyRate;
    int cores;
    int memory;
    int disk;

    public String toString(){
        return "Server [type=" + type + ", limit=" + limit + ", bootup time=" + bootupTime + ", hourly rate=" + hourlyRate + ", cores=" + cores + ", memory=" +memory + ", disk " + disk + "]";
    }    

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public int getLimit(){
        return limit;
    }

    public void setLimit(int limit){
        this.limit = limit;
    }

     public int getBootupTime(){
        return bootupTime;
    }

    public void setBootupTime(int bootupTime){
        this.bootupTime = bootupTime;
    }

    public Double getHourly(){
        return hourlyRate;
    }

    public void setHourly(Double hourlyRate){
        this.hourlyRate = hourlyRate;
    }

    public int getCores(){
        return cores;
    }

    public void setCores(int cores){
        this.cores = cores;
    }

    public int getMemory(){
        return memory;
    }

    public void setMemory(int memory){
        this.memory = memory;
    }

    public int getDisk(){
        return disk;
    }

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
        List<Server> servers = parseConfigXML();
        for (int temp = 0; temp < servers.getLength(); temp++){
            //compare based on cores
              

        }


    }
   
   
    private static List<Server> parseConfigXML() throws ParserConfigurationException, SAXException, IOException{
    
        List<Server> servers = new ArrayList<Server>();
        Server server = null;
       
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("/home/francesca/ds-sim/src/pre-compiled/ds-sample-config01.xml"));
        document.getDocumentElement().normalize();
        NodeList serverList = document.getElementsByTagName("server");
        for (int temp = 0; temp < serverList.getLength(); temp++){
            Node node = serverList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element serverElement = (Element) node;
                //Create new Server object
                server = new Server();
                server.setType(serverElement.getElementsByTagName("type").item(0).getTextContent());
                server.setLimit(Integer.parseInt(serverElement.getAttribute("limit")));
                server.setBootupTime(Integer.parseInt(serverElement.getAttribute("bootupTime")));
                server.setHourly(Double.parseDouble(serverElement.getAttribute("hourlyRate")));
                server.setCores(Integer.parseInt(serverElement.getAttribute("cores")));
                server.setMemory(Integer.parseInt(serverElement.getAttribute("memory")));
                server.setDisk(Integer.parseInt(serverElement.getAttribute("disk")));

                //Add server to list
                servers.add(server);
            }
        }
        return servers;
    }
}
  
