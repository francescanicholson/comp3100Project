//Francesca Nicholson
//45903824

//Java import packages for network, I/O, ArrayLists, XML parsers(DOM) & exceptions
import java.io.*;
import java.io.File;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.xml.sax.SAXException;

class dsClientSJF {

  public static void main(String args[]) {
    //used for authentication of username
    String authName = System.getProperty("user.name");
    Socket s = null;
    String response = "";
    int count = 0;

    try {
      //server port used for ds-client as specified in marking criteria
      int serverPort = 50000;
      s = new Socket("localhost", serverPort);
      //reader and output streams for I/O communications of ds-client and ds-server
      BufferedReader in = new BufferedReader(
        new InputStreamReader(s.getInputStream())
      );
      DataOutputStream out = new DataOutputStream(s.getOutputStream());

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
      while (!response.equals("NONE")) {
        //telling ds-server that ds-client is ready for starting job information/scheduling
        out.write(("REDY\n").getBytes());
        //gathering response from ds-server
        response = in.readLine();

        //printing out response from ds-server
        System.out.println(serverResponse(response));
        if (response.startsWith("JOBN")) {
          String[] jobDetails = response.split(" ");
          int detailsLength = jobDetails.length;

          String jobId = jobDetails[2];

          //requesting server info from ds-server
          out.write(
            (
              "GETS Capable " +
              jobDetails[detailsLength - 3] +
              " " +
              jobDetails[detailsLength - 2] +
              " " +
              jobDetails[detailsLength - 1] +
              "\n"
            ).getBytes()
          );

          //gathering response from ds-server
          response = in.readLine();

          //splits the server data into chunks of info (as seperated by " ")
          String[] serverData = response.split(" ");

          List<String> myServers = new ArrayList<String>();

          out.write("OK\n".getBytes());
          //response = in.readLine();

          for (int i = 0; i < Integer.parseInt(serverData[1]); i++) {
            response = in.readLine();
            //add to my server
            myServers.add(response);
            System.out.println(serverResponse(response));
          }

          out.write("OK\n".getBytes());
          response = in.readLine();

          //printing out response from ds-server
          System.out.println(serverResponse(response));

          String trgServer = myServers.get(0);
          String[] trgSplit = trgServer.split(" ");

          if (count > 0) {
            //Collections.shuffle(myServers);
            trgServer = myServers.get(0);
            trgSplit = trgServer.split(" ");
            int waitJob = Integer.parseInt(trgSplit[7]);
            int runJob = Integer.parseInt(trgSplit[8]);
            String status = trgSplit[2];

            boolean test = true;
            System.out.println("TARGET: " + trgServer);
            if (waitJob > 0 || runJob > 0) {
              for (int i = 0; i < myServers.size() && test; i++) {
                trgServer = myServers.get(i);
                trgSplit = trgServer.split(" ");
                waitJob = Integer.parseInt(trgSplit[7]);
                runJob = Integer.parseInt(trgSplit[8]);
                status = trgSplit[2];

                if (waitJob == 0 && runJob == 0 || status.startsWith("idle")) {
                  trgServer = myServers.get(i);
                  test = false;
                }

                if (status.startsWith("active")) {
                  if (myServers.size() <= 50) {
                    if (waitJob > 2 && runJob > 2) {
                      myServers.remove(trgServer);
                    }
                    if (waitJob == 0) {
                      trgServer = myServers.get(i);
                      test = false;
                    }
                  }
                  if (myServers.size() > 50) {
                    if (waitJob > 0 && runJob > 2) {
                      myServers.remove(trgServer);
                    }

                    if (waitJob == 0) {
                      trgServer = myServers.get(i);
                      test = false;
                    }
                  }
                }
              }
            }

            out.write(
              (
                "SCHD " + jobId + " " + trgSplit[0] + " " + trgSplit[1] + "\n"
              ).getBytes()
            );

            response = in.readLine();

            //printing out response from ds-server
            System.out.println(serverResponse(response));
          }

          if (count == 0) {
            //schd jobid servertype serverid
            out.write(
              (
                "SCHD " + jobId + " " + trgSplit[0] + " " + trgSplit[1] + "\n"
              ).getBytes()
            );
            response = in.readLine();

            //printing out response from ds-server
            System.out.println(serverResponse(response));
            count++;
          }
        }
      }

      //used to proceed to end
      out.write("OK\n".getBytes());
      //gathering response from ds-server
      response = in.readLine();
      //quits simulation
      out.write(("QUIT\n").getBytes());
      out.flush();
      out.close();
      s.close();
    } catch (UnknownHostException e) { //error catching
      //used to catch unknown socket host exceptions
      System.out.println("Sock:" + e.getMessage());
    } catch (EOFException e) {
      //used to catch end of file exceptions
      System.out.println("EOF:" + e.getMessage());
    } catch (IOException e) {
      //used to catch input/output exceptions
      System.out.println("IO:" + e.getMessage());
    }
    //trying to close the system if the socket is null
    if (s != null) try {
      s.close();
    } catch (IOException e) {
      //used to catch input/output exceptions
      System.out.println("close:" + e.getMessage());
    }
  }

  //used to see server response
  public static String serverResponse(String serverResponse) {
    return "RCVD: " + serverResponse;
  }
}
