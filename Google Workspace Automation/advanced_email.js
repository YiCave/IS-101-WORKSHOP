function onFormSubmit() {
  
  Logger.log("--- New Method Triggered ---");

  try {
    const form = FormApp.getActiveForm();
    const allResponses = form.getResponses();
    
    if (allResponses.length === 0) {
      Logger.log("Error: No responses found in the form.");
      return;
    }

    const latestResponse = allResponses[allResponses.length - 1];
    const itemResponses = latestResponse.getItemResponses();
    
    const responseDict = {};
    for (let i = 0; i < itemResponses.length; i++) {
      const questionTitle = itemResponses[i].getItem().getTitle().trim();
      const answer = itemResponses[i].getResponse();
      responseDict[questionTitle] = answer;
    }

    Logger.log("Processing last response. Found questions: " + Object.keys(responseDict));
    Logger.log("responseDict content: " + JSON.stringify(responseDict, null, 2));

    //function for fuzzy matching
    function getResponseValue(dict, keyword) {
      for (const key in dict) {
        if (key.toLowerCase().includes(keyword.toLowerCase())) {
          return dict[key];
        }
      }
      return "";
    }

    const collectedEmail = latestResponse.getRespondentEmail();
    
    const emailFromQuestion = getResponseValue(responseDict, '电子邮件地址') 
                           || getResponseValue(responseDict, 'Email Address');
    const email = collectedEmail || emailFromQuestion;

    if (!email) {
      Logger.log("Error: Could not find respondent's email address. Aborting script.");
      Logger.log("Collected Email (from GForm setting): " + collectedEmail);
      Logger.log("Email from Question (from form field): " + emailFromQuestion);
        /*Google Forms automatically translates question titles based on the form's default language.
          Since our committee uses Chinese as the default language, the "Email Address" field may appear as its Chinese translation.
          The same applies to any other languages (e.g., Malay default uses: responseDict['Alamat E-mel *']).*/
      Logger.log("Available questions for email: " + responseDict['电子邮件地址 *'] + " or " + responseDict['Email Address *']);
      return; 
    }
    
    Logger.log("Email found: " + email);

    //use fuzzy matching for all fields
    const fullName = getResponseValue(responseDict, 'Full Name') || "Participant";
    const phone = getResponseValue(responseDict, 'Phone Number');
    const faculty = getResponseValue(responseDict, 'Faculty');
    const programme = getResponseValue(responseDict, 'Department');
    const year = getResponseValue(responseDict, 'Year');

    const subject = "Registration Confirmation – Build & Break: Information Systems 101 Workshop";

    let greetingLine = "";

    if (faculty && faculty.toLowerCase().includes("fcsit") && programme && year) {
      greetingLine = `Hi <b>${fullName}</b>, from the <b>${programme}</b> programme (${faculty}, ${year}),`;
    } else if (faculty) {
      greetingLine = `Hi <b>${fullName}</b>, from <b>${faculty}</b>,`;
    } else {
      greetingLine = `Hi <b>${fullName}</b>,`; 
    }

    const htmlBody = `
    <p>${greetingLine}</p>

    <p>This email is to notify you that you are successfully registered for the <b>Build & Break: Information Systems 101 Workshop</b>.</p>

    <p>Below is the link to the workshop resources. Kindly go through it and follow the instructions inside:</p>

    <p><a href="https://github.com/YiCave/IS-101-WORKSHOP" target="_blank"><b>Workshop GitHub Repository</b></a></p>

    <p>
      <b>Date:</b> 20 November 2025 (Thursday)<br>
      <b>Time:</b> 10:30 a.m. - 12:30 p.m.<br>
      <b>Venue:</b> The Cube, Block A, Faculty of Computer Science and Information Technology, University of Malaya
    </p>

    <p>Here is a cat for you to look at while waiting for the workshop!</p>

    <img src="https://cataas.com/cat" alt="cat" width="300" style="border-radius:10px;">

    <p>If you have any questions, feel to contact:<br>
      <b>Director:</b> Tan Hui Zhe (+601X-XXX XXXX)<br>
      <b>Vice Director:</b> Lee Qian Yi (+601Y-YYY YYYY)<br>
      <b>Secretary:</b> Saw Yong Quan (+601Z-ZZZ ZZZZ)</p>

    <p>Hope to see you on that day! Make sure to have your laptops charged before coming.</p>

    <p>Best regards,<br>
    <b>PEKOM IS Workshop Committee</b></p>
    `;

    GmailApp.sendEmail(email, subject, '', { htmlBody: htmlBody });
    Logger.log("Email sent successfully to " + email);

  } catch (err) {
    Logger.log("An error occurred: " + err.message);
    Logger.log("Stack trace: " + err.stack);
  }
}