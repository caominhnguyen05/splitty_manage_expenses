# OOPP Template Project

This repository contains the template for the OOPP project.

# Installation
Clone this repository:

```bash
git clone <repository_url>
```

Navigate to the project directory:

```bash
cd project-directory
```
(Optional) If you need to customize any configurations, update the build.gradle file accordingly.

# Usage
To build the project, execute the following command in your terminal:

```bash
gradle build
```

To start the server of the project, execute the following command in your terminal:

```bash
gradle bootRun
```

To start the client of the project, execute the following command in your terminal:

```bash
gradle run
```

Simply use Ctrl+C in the terminal to end either the server or client.

# HCI Features
**Use these keyboard shortcuts and navigations to easily switch between pages or do some important actions:**

<details>
  <summary>Keyboard shortcuts</summary>

***Invitation View***

- Press CTRL + ENTER after entering emails to send invites.

***Start Screen***<br>
- When you are in the list of "Recently Viewed Events", press D to delete an event, 
or ENTER to go the Event Overview page of that event.
- Press CTRL + A to go to Admin Login page.
- Press CTRL + L to open language indicator.

***Event Overview***

* Press D to delete an expense when you are in one of the expense lists.
* CTRL + I to go to Send Invites page.
* Ctrl + E add a new expense.
* Ctrl + S to go to Statistics page
* Ctrl + D to go to Settle Debts page.
* Ctrl + P to add a participant.
* Ctrl + L to open the language indicator.
* Ctrl + W to close the edit/remove participant dialog.
* Ctrl + K to open the currency indicator

Open Debts
* Ctrl + P to show Payment Log
* After typing in participant's name, press ENTER to see the debts of that person.
* Ctrl + K to open the currency indicator

</details>
<details>
  <summary>Keyboard navigations</summary>

***General***
* Every page supports the use of the TAB key to navigate between page elements 
in a left - right, top - bottom manner.
* ARROW keys can also be used to move in any direction between page elements (right to left, bottom to top, etc.).
* The language indicators in Start Screen and Event Overview can be opened by pressing CTRL + L,
then to choose a language, use UP and DOWN arrow keys to navigate and press ENTER to change to the language of your choice.

***Start Screen***
* After typing in an event name in the Create Event text box, press ENTER to create the event.
* After typing in an event code in the Join Event text box, press ENTER to join that event.

***Add/Edit participant***
* When typing in any texts field, press ENTER to submit a participant's details.
* If a confirmation dialog shows up, use TAB or LEFT/RIGHT keys to switch between "Cancel" and "OK" buttons.
* Use UP and DOWN keys to move between text fields.

***Add/Edit Expense***
* When typing in any texts field, press ENTER to submit the expense's details.
* Use UP and DOWN, or TAB keys to move between texts fields.
</details>

## HTTP Long Polling
The client uses long polling to update name change of the events on the start screen list of saved events. So when the name of an event saved by the user changes, it's also reflected on the start screen. The client side implementation is in `ServerUtils.java`. Client creates a new listener using the `registerForUpdates` method. Server side implementation is in `EventController.java`, method `getUpdate`.

## Web Sockets
The client uses websockets to listen for any changes made to the event and refreshes the screen accordingly. The client side implementation is in `ServerUtils.java`. Client maintains only one web socket connection at a time. A new communication is started with `openWebSocketEventListener` method. On server side, all relevant classes are under `websockets` directory.


# Mail Feature
There is a mail sender service that is used in the following situations:

    1. Sends invites to the addresses filled in the corresponding text box from Invite Screen.
    2. Sends a payment reminder if both debtor's and credior's emails are available ( the creditor is added to Cc).
    3. Sends a confimation email to a new added participant, if email available.
    4. Sends a credentials updated confirmation email to a participant who has been edited, if email available.

### Note for the instructors
There are a lot of UI tests in this project which we were forced to 
comment out because of the limitations of the testing environment. 
As the testing environment apparently likes to store the scenes in 
the memory, and due to some images that we had in the project, we 
got a lot of OutOfMemory errors. We are aware that these tests might 
not count towards our grades, but please still take a look at them 
so our struggles have not been in vain!

