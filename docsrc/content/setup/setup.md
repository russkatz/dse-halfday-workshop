---
title: Setup Instuctions
weight: 301
menu:
  main:
    parent: Setup Instructions
    identifier: setup
    weight: 31
---

## DSE ½ Day Workshop Setup Instructions

The following instructions will guide you through the process of deploying a specified number of clusters via the Vanguard Template for use with the DSE ½ Day Workshop.  The resulting clusters will consist of three (3), m1.xlarge instances, each with their own Opscenter and Workbook.  The Workbook will automatically be populated with the hands-on exercise instructions for the students.

##  Step #1:  Create the Student Clusters

To get started, deploy the student clusters using Asset Hub or the following Vanguard Template:

 https://selfservice-4.rightscale.com/catalog/launch/58d61b3736f9d036502b4d55

Enter number of clusters required.  1 per student.

When using the manual template:

- SSH Private Key: copy the ssh key to dseworkshop.priv
- Use the following github repo URL: git@github.com:russkatz/dse-halfday-workshop.git

## Step #2:  Create Cluster Assignment Spreadsheet

Once deployed you will have a list of clusters and their “Node 0” IPs.  Enter the IP’s in a copy of the following Google Docs Spreadsheet:

https://docs.google.com/a/datastax.com/spreadsheets/d/1fAlogTPddtBvd7hAjFct2SbHVZbB5HIkw2T2FGUPdJQ/edit?usp=sharing

You will later use this spreadsheet to assign clusters to each student.

## Step #3:  Create the SSH Keys

The students will need SSH keys so that they can SSH to their cluster in order to complete the hands-on exercises in the DSE Analytics Workbook. Mac users will be able to use the “dseworkshop.priv” key saved in Step #1.  However, you will need to create a key for Windows users.   In order to do so, you will need to convert “dseworkshop.priv” for use with Windows Putty:

### Download and launch PuttyGen

  https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html

- Click Conversions from the PuTTY Key Generator menu and select Import key.

- Navigate to dseworkshop.priv and click Open.

- Under Actions / Save the generated key, select Save private key.

- Save the private key as dseworkshop.ppk.

## Step #4:  Make the SSH Keys Available for Download

Now that you have SSH keys for both the Mac students, “dseworkshop.priv” and the Windows students, “dseworkshop.ppk”, you will need to create a zip file and push that zip file to github:

Push login keys to github.  If this file already exists, delete first and then push the new keys: https://github.com/russkatz/dse-halfday-workshop/blob/master/files/dse_workshop_keys.zip


## Step #5:  Send a Welcome Email  

Copy the instructions below and send along with a “Welcome” email to the class participants.  You should be able to obtain a list of student email addresses from the Marketing Event Coordinator. 


### SAMPLE WELCOME EMAIL

```
Welcome to the DataStax Enterprise ½ Day Workshop!  In preparation for our day together I’d like to give you your DataStax Enterprise cluster login instructions and credentials. 
 
Please have this completed before class.  If you get stuck, feel free to come early to class and someone will be there to help you get connected.
 
Thank you and I look forward to working with you on XXXX.
 
- Your Name
Solutions Engineer, Datastax
```



### Each student will need:

- Username: ds_user
- Your_Assigned_IP:   The following link will bring you to a Google Spreadsheet where you will assign yourself to a Cluster Seed Node IP.  Please put your name in the column next to an available IP to claim that as yours.  Please only claim one IP.

  https://docs.google.com/a/datastax.com/spreadsheets/d/1fAlogTPddtBvd7hAjFct2SbHVZbB5HIkw2T2FGUPdJQ/edit?usp=sharing

SSH key: The following link will take you to a GitHub account where you will be able to download an encrypted zip file with both the Windows and Mac keys included. The zip file password is The!Moment
 
https://github.com/russkatz/dse-halfday-workshop/blob/master/files/dse_workshop_keys.zip
 
Workbook URL:    http://<Your_Assigned_IP>:9091
OpsCenter URL:  http://<Your_Assigned_IP>:8888

### If you are on Windows:

1. Download Putty Key

  - https://github.com/russkatz/dse-halfday-workshop/blob/master/files/dse_workshop_keys.zip

1. Extract and use dseworkshop.ppk

1. Download and launch Putty

  - https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html

1. Create a new session

  - ```
Hostname: <Node 0 IP> ```
  - ```
Port: 22
```
  - ```
Connection Type: SSH
```
  - ```
Saved Session: DSE Workshop (click Save)
```

1. Click Connection -> SSH -> Auth (In the left pane)
1. Under Private Key file for Auth click Browse
1. Browse to dseworkshop.ppk
1. Click Session (Left pane)
1. Click DSE Workshop
1. Click Save
1. Click Open

  - Username: ds_user

1. Accept the key signature 

### If you are using a Mac:

1. Download https://github.com/russkatz/dse-halfday-workshop/blob/master/files/dse_workshop_keys.zip
1. Unzip and extract dseworkshop.priv by clicking on the zip file in your Downloads directory
1. Enter the password: The!Moment
1. Open a Terminal Window:

  - ```
cd ~Downloads/dseworkshop_keys
```
  - ```
cp dseworkshop.priv  /tmp
```
  - ```
chmod 600 /tmp/dseworkshop.priv
```
  - ```
ssh -i /tmp/dseworkshop.priv ds_user@<Your_Assigned_IP>
```

1. Type yes to accept key signature
