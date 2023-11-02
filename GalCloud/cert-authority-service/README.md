# Overview
Galsie communicates with the CSA's DCL (distributed compliance ledger) for the certification of Galsie Devices & the attestation of matter devices


# DCL
The CSAs Distrubuted Compliance Ledger is a distributed network/chain of nodes that run on blockchain technology that acts as a provider for matter certification transactions.
- Can connect to the DCL by:
  - CLI (through gRPC - make sure of that)
  - REST
In both cases, must connect to the 'main-net' chain hosted on 'on.dcl.csa-iot.org'
  - Check https://github.com/zigbee-alliance/distributed-compliance-ledger/tree/master/deployment/persistent_chains for a list of chains
  - Each directory is a running chain, inspecting it will show you information about how to connect to it. 
