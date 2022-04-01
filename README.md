# comp3100Project

Ds-Client is a vanilla model of the client-side simulation to work alongside with ds-server. This client simulation is able to both schedule and dispatch jobs from ds-server. The ds-client will be able to read from the ds-server's supplied XML file of information of its servers, and then be able to choose the most appropriate server to run jobs using a Largest Round Robin (LRR) (also known as Round Robin) scheduling algorithm. This means that it will choose a server to run the job if it is the largest in terms of cores, but also if it is the first of its size.
