# 2025 PEKOM IS 101 Workshop Documentation

## Introduction
This workshop is organized as part of a student-led initiative under PEKOM, the student club of the Faculty of Computer Science and Information Technology, University of Malaya.
The committee members of this workshop are undergraduate students from the Faculty of Computer Science and Information Technology, University of Malaya.
The aim of this workshop is to introduce foundational concepts of Information Systems through hands-on activities and practical demonstrations.

## Requirements and Setup
This workshop will require several pre-installation and setup. Participants are advised to do all of these installations before attending the workshop on 20th November 2025, to ensure a smooth workshop session. For codes that are using the programming language Java, the demo during the workshop will be done using Apache Netbeans IDE.

### 1. Google Workspace Automation
- Google Form preperation
- Use any Google Account of yours and create a simple Google Form before attending the workshop.
- could include simple fields like name, Gmail, course, year of studies etc
- **IMPORTANT**: field to record Gmail is a **MUST** for this program to work.

### 2. Database

#### 🗃️ **i) MySQL Installation Guide**

🪟 **Windows OS**
1. Go to [https://www.mysql.com/](https://www.mysql.com/).
2. Navigate to **DOWNLOADS** → **MySQL Community (GPL) Downloads**.
3. Select **MySQL Installer for Windows**.  
   - Ensure the version is the latest (e.g., *8.x*).  
   - Verify the OS is **Microsoft Windows**.
4. Click **DOWNLOAD** on the *mysql-installer-web-community-8.x.x.msi* option.  
   - Then choose **“No thanks, just start my download.”**
5. Open the downloaded installer file.
6. Under **Setup Type**, select **Custom**, then click **Next**.
7. Under **MySQL Servers**, select the latest version (click the right-pointing arrow to add it).
8. Under **Applications**, select the latest version of **MySQL Workbench**, then click **Next**, followed by **Execute** to install.
9. Continue clicking **Next** until you reach the **Accounts and Roles** page.  
   - Set your **MySQL Root Password** as `password` (for simplicity during the workshop).
10. Click **Next**. Under the **Windows Service** page, ensure **“Start the MySQL Server at System Startup”** is **ON**.
11. Click **Next**, then **Execute** to complete the setup.

---

🍎 **macOS**
1. Follow steps **1–4** from the Windows section, but instead of choosing *MySQL Installer for Windows*, select **MySQL Community Server**.  
   - Download the **macOS 12 (or latest) DMG Archive**.
2. Open the downloaded file, and when prompted, click **Allow** to run the installer.
3. Agree to the license terms, then click **Install**.
4. When prompted, choose **“Use Strong Password Encryption”**, then click **Next**.
5. Set your **Root Password** as `password`, then click **Finish** and **Close**.
6. To install **MySQL Workbench**, go back to [mysql.com](https://www.mysql.com/) → **DOWNLOADS** → **MySQL Community (GPL) Downloads** → **MySQL Workbench**.
7. Click **Download**, then **“No thanks, just start my download.”**
8. Open the downloaded DMG file, and **drag and drop** the *MySQLWorkbench* icon into your **Applications** folder.
9. To verify that the MySQL server is running:  
   - Open **System Preferences** → look for **MySQL** → click **Start MySQL Server**.
10. Check the box for **“Start MySQL when your computer starts up.”**
11. To launch Workbench: **Finder** → **Applications** → **MySQL Workbench**.

---

✅ **Tips for Students**
- If you are still unsure, you can follow this tutorial video https://youtu.be/oPV2sjMG53U?si=nUT3JjtsEPFFhzta . Windows (2:23), MacOS (6:06).
- Remember your root password — you’ll need it to connect Workbench to the server.  
- If MySQL Workbench fails to connect, ensure the MySQL server is running.  
- For macOS users, if the *MySQL* icon doesn’t appear in System Preferences, try reinstalling from the DMG file.

--- 

#### ii) ⚙️ Setting Up MySQL JDBC Connector in Apache NetBeans IDE

🔽 **Download the JDBC Connector**
1. Go to [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/).
2. Under **Select Operating System**, choose **Platform Independent**.
3. Click **Download** for *Platform Independent (Architecture Independent), ZIP Archive* (e.g., `mysql-connector-j-9.5.0.zip`).
   - Then choose **“No thanks, just start my download.”**
4. Once downloaded, **extract** the ZIP file (right-click → *Extract All*).

🧩 **Configure JDBC Driver in NetBeans**

5. Open **Apache NetBeans IDE**.
6. In the left section of the interface, go to the **Services** tab.
7. Expand **Databases**, then select **New Connection...**
8. Under **Driver**, ensure **MySQL (Connector/J driver)** is selected.  
   - If it’s missing, click **Add...** on the right and navigate to the extracted folder.  
   - Select the `.jar` file (e.g., `mysql-connector-j-9.5.0.jar`).
9. Click **Next >**.
10. Enter your MySQL **Root Password** (the one you set earlier during installation).  
    - If the password is correct and the MySQL server is running, you should see **“Connection succeeded.”**  
    - Then click **Next >**.
11. Under **Choose Database Schema**, click **Next >**, and on the next page, click **Finish**.

💻 **Link JDBC to Your Java Project**

12. Create a **Java with Ant** project in Apache NetBeans IDE.
13. In the **Projects** tab, expand your project folder.
14. Right-click **Libraries** → select **Add JAR/Folder...**
15. Navigate to the extracted JDBC connector folder and select the `.jar` file (e.g., `mysql-connector-j-9.5.0.jar`).
16. Click **Open** to add the connector to your project libraries.

---

✅ **Verification**
- Create a simple Java file and try connecting to your database using `DriverManager.getConnection(...)`.  
- If the connection is successful, your JDBC setup is complete! 🎉

Example test snippet:
```java
import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
         // for url, user and password please check and replace with the one you set
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "password";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


✅ **Tips for Students**
- If you are still unsure, you can follow this tutorial video https://youtu.be/rgF-5CwTZeE?si=JDNICvMDh-XpiNZL , using the timestamp 1:28 - 6:10.
- Ensure your MySQL Server is running before testing the connection.
- If MySQL Server is not running: Win + R -> services.msc -> find MySQL -> Right click, Start
- If you get a “Driver not found” error, double-check that the .jar file is correctly linked under Libraries.
- Keep the connector file in a permanent location — moving or deleting it may break the link in your project.
  
---

### 3. Encryption Session
- no prior setup required
- source code is included in \Encryption 
