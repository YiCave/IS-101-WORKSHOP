function sendEmail(e) {
  var name = e.values[1];     
  var email = e.values[2];    
  var course = e.values[3];   

  var subject = "Registration for PEKOM IS 101 workshop";

  var htmlBody = `
    <p>Hi <strong>${name}</strong>, from the <strong>${course}</strong> department,</p>
    <p>This email is to notify you that you are successfully registered for the <strong>PEKOM IS 101 Workshop</strong>.</p>
    <p>Below is the link to the workshop resources. Kindly go through it and follow the instructions inside:</p>
    <p><a href="https://github.com/YiCave/IS-101-WORKSHOP.git">Workshop GitHub Repository</a></p>
    <p><strong>Date:</strong> 20th November 2025<br>
       <strong>Time:</strong> 10:30 a.m. – 12:30 p.m.<br>
       <strong>Venue:</strong> The Cube, Block A, Faculty of Computer Science and Information Technology</p>
    <p>Here is a cat for you to look at while waiting for the workshop!</p>
    <p><img src="https://i.imgur.com/XSVb4S4.jpeg" width="500" alt="cat" /></p>
    <p>If you have any questions, feel free to contact Tan Hui Zhe (+601x-xxx xxxx).</p>
    <p>Hope to see you on that day! Make sure to have your laptops charged before coming.</p>
  `;

  MailApp.sendEmail({
    to: email,
    subject: subject,
    htmlBody: htmlBody
  });
}