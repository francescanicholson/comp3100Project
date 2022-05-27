//Francesca Nicholson
//45903824

//Java import packages for network, I/O, ArrayLists, & exceptions
import java.io.*;
import java.io.File;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
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
          //to store the list of servers from the GETS Capable
          List<String> myServers = new ArrayList<String>();
          //to proceed to next step
          out.write("OK\n".getBytes());

          //to add every server that is sent after the GETS command
          for (int i = 0; i < Integer.parseInt(serverData[1]); i++) {
            //gathering response from ds-server
            response = in.readLine();
            //add server to server list
            myServers.add(response);
            //print out server
            System.out.println(serverResponse(response));
          }

          //to proceed to next step
          out.write("OK\n".getBytes());
          //gathering response from ds-server
          response = in.readLine();
          //printing out response from ds-server
          System.out.println(serverResponse(response));
          //get the first server provided by the GETS command
          String trgServer = myServers.get(0);
          //split up the server info so its readable and easily accessable
          String[] trgSplit = trgServer.split(" ");

          //after the first job has been scheduled
          if (count > 0) {
            //get the first server provided by the GETS command
            trgServer = myServers.get(0);
            //split up the server info so its readable and easily accessable
            trgSplit = trgServer.split(" ");
            //stores the server's waiting job info
            int waitJob = Integer.parseInt(trgSplit[7]);
            //stores the server's running job info
            int runJob = Integer.parseInt(trgSplit[8]);
            //stores the server's status (inactive, idle, active) info
            String status = trgSplit[2];
            //used to exit out of loops easily
            boolean stop = true;

            //if the server chosen (the first from GETS Capable) has a running or waiting job
            if (waitJob > 0 || runJob > 0) {
              //traverse through the list of servers
              for (int i = 0; i < myServers.size() && stop; i++) {
                //get the current server in the loop
                trgServer = myServers.get(i);
                //split up the server info so its readable and easily accessable
                trgSplit = trgServer.split(" ");
                //stores the server's waiting job info
                waitJob = Integer.parseInt(trgSplit[7]);
                //stores the server's running job info
                runJob = Integer.parseInt(trgSplit[8]);
                //stores the server's status (inactive, idle, active) info
                status = trgSplit[2];

                //if the server chosen has no waiting or running jobs / is idle
                if (waitJob == 0 && runJob == 0 || status.startsWith("idle")) {
                  //choose the server as the one to schedule
                  trgServer = myServers.get(i);
                  //get out of the loop
                  stop = false;
                }

                //if the chosen server's status is active
                if (status.startsWith("active")) {
                  //if the server size is less or equal to 50
                  if (myServers.size() <= 50) {
                    //if the server has more than 2 waiting or running jobs
                    if (waitJob > 2 && runJob > 2) {
                      //remove the server from the server list
                      myServers.remove(trgServer);
                    }
                    //if the server has no waiting jobs
                    if (waitJob == 0) {
                      //choose the current server
                      trgServer = myServers.get(i);
                      //quit the loop
                      stop = false;
                    }
                  }
                  //if the server size is more than 50
                  if (myServers.size() > 50) {
                    //if the server has waiting jobs or more than 2 running jobs
                    if (waitJob > 0 && runJob > 2) {
                      //remove the server from the server list
                      myServers.remove(trgServer);
                    }
                    //if the server has no waiting jobs
                    if (waitJob == 0) {
                      //choose the current server
                      trgServer = myServers.get(i);
                      //quit the loop
                      stop = false;
                    }
                  }
                }
              }
            }
            //schedule the job to the server as chosen through the previous methods
            out.write(
              (
                "SCHD " + jobId + " " + trgSplit[0] + " " + trgSplit[1] + "\n"
              ).getBytes()
            );
            //reading response from ds-server
            response = in.readLine();
            //printing out response from ds-server
            System.out.println(serverResponse(response));
          }

          //on the first job scheduling
          //schedule the first server from GETS Capable
          if (count == 0) {
            //schd jobid servertype serverid
            out.write(
              (
                "SCHD " + jobId + " " + trgSplit[0] + " " + trgSplit[1] + "\n"
              ).getBytes()
            );
            //reading response from ds-server
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
