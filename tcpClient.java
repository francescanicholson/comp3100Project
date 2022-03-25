/*import java.net.*;  
import java.io.*;
import java.util.concurrent.TimeUnit;

class tcpClient{  
public static void main(String args[])throws Exception{  
    Socket s = new Socket("localhost",50000);
    DataInputStream din=new DataInputStream(s.getInputStream());  
    DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    
    String server = "", msg = "";
    
    while(!server.equals("BYE")) {
            server = br.readLine();
            dout.write(write(("HELO\n").getBytes());
            dout.flush();
            msg = br.readLine();
            System.out.println("Server: " + msg);
        }
        dout.close();
        s.close();
    }
}
*/

import java.net.*;
import java.io.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


class tcpClient {
    public static void main (String args[]) {
// arguments supply message and hostname of destination
    Socket s = null;
    try{
        int serverPort = 50000;
        s = new Socket("localhost", serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        DataOutputStream out =
        new DataOutputStream( s.getOutputStream());
        out.write(("HELO\n").getBytes()); // UTF is a string encoding see Sn 4.3
        String data = in.readLine();
        System.out.println("Received: "+ data);
        out.write(("AUTH fransis\n").getBytes()); // UTF is a string encoding see Sn 4.3
        String auth = in.readLine();
        System.out.println("Received: "+ auth);
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

    String xmlFilePathname = "ds-sample-config01-2.xml";
		File xmlFile = new File(xmlFilePathname);

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(xmlFile);

		NodeList nodeList = document.getElementsByTagName("servers");

		for (int i = 0; i <= nodeList.getLength() - 1; i++) {

			Node node = nodeList.item(i);
			Element element = (Element) node;

			System.out.println("---- Server: " + (i + 1) + " ----");
			System.out.println("Type: " + getElementText(element, "type"));
			System.out.println("Limit: " + getElementText(element, "limit"));
			System.out.println("Bootup Time: " + getElementText(element, "bootupTime"));
			System.out.println("Hourly Rate: " + getElementText(element, "hourlyRate"));
            System.out.println("Cores: " + getElementText(element, "memory"));
            System.out.println("Disk: " + getElementText(element, "disk"));

		}
		
	

	public static String getElementText(Element element, String tagName) {
		return element.getElementsByTagName(tagName).item(0).getTextContent();
	}

    }
}


   

