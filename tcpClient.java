import java.net.*;
import java.io.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


class tcpClient {

    public static String getElementText(Element element, String tagName) {
		return element.getElementsByTagName(tagName).item(0).getTextContent();
	}


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
        out.write(("AUTH francesca\n").getBytes()); // UTF is a string encoding see Sn 4.3
        String auth = in.readLine();
        System.out.println("Received: "+ auth);
       

        // DOM READER FOR XML FILE

        String xmlFilePathname = "ds-sample-config01.xml";
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

        out.write(("REDY\n").getBytes());
        String readData = in.readLine();
        System.out.println("Received: "+ readData);
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
