# Meeting Week 7 - NOTES

 Key | Value |
| --- | --- |
| Date: | 26-03-2024 |
| Time: | 13:45 |
| Location: | DW Project Room 4 |
| Chair | Bas Bruijnis |
| Minute Taker | Nguyen Cao Minh |
| Attendees | Jochem Kralt, Jakub Florek, Kiarash Karimi, Rares Patrut |


### Agenda items:
- **Opening by chair (1 min)**
- **Approval of the agenda - Does anyone have any additions? (1 min)**
<br>No additions
- **Announcements by the TA**<br>
Timeslot for the product presentation is available to see. Our timeslot is 13:35 - 14:05 on 16/04/2024.
- **Talking Points:** (Review+Updates/ Discuss/ Decision Making)
  - **How is everyone doing, What are the impressions after last weeks tasks, Is everyone finished with their work? (6 min)** <br>
  The Websockets connection/implementation is done and all admin functionality has been implemented. The language switching functionality is done for all views although there is a minor issue causing NullPointerException. The pie chart for Statistics page have been implemented based on expenses but problems occur when refreshing it many times. Debts have been simplified and calculation for the balance of a chosen creditor/debtor has been implemented. The exception caused when editing an expense has been solved, but there is a new problem that after editing expense, the debtors get duplicated many times in the database.
    - Payment/Debt calculations, template files and mock data removed, websockets, languages, fixed bugs (Participants and Expenses) admin, accessibility assignment, pop-ups removed, ... more?

  - **Present the current progress to the TA (4 min)**

  - **Assignments: How did accessibility go? This week draft for Product Pith (Get TA Feedback). Code Contributions and Code Reviews is coming up, are we ready? (5 min)** <br>
    - For accessibility, we have got "Color Contrast", "Informative Feedback", "Confirmation for Key Actions", "Error Messages" and "Logical Navigation". We still need to do "Keyboard Shortcuts", "Keyboard Navigation", "Multimodel Visualization" (which needs to be considered in the new design), and "Supporting undo actions". <br>
    - Project pitch: We should meet some time this week to discuss how to do this project pitch draft (which format to do). The TA pointed out that beyond talking about the functionality of our application, we should also talk about what we did beyond requirements/ what makes the app stands out. She also mentioned that the oral examination will be all related to the app, not purely about lectures contents.
  From now on, make merge request smaller, with smaller changes
  
  - **MR Groups (2 min)** <br>
  As everyone agreed, from now we will include everyone in the group as reviewers for our Merge Request, so that everyone knows how the app/different functionalities works which is beneficial for our product pitch.
  - **Testing, What has been done/What needs to be done (dependency injection w/Mockito for example) (3 min)** <br>
  We should start on testing the UI, and everyone should do a part of it. Based on the feedback from Testing assignment, we should do more testing on functional code in commons and client, now we have mainly tested the endpoints. Also, we need to include test in every merge request.
  - **Decide the progress for next week (17 min)**
    - **Current open issues**
    - **New issues** <br>
We will do the following this week:

      |Member | Task |
      | --- | --- |
      | Rares | Fix problems when editing expense (duplicate debtors), maybe look into implementing currency change |
      | Kiarash | Refine and fix bugs when calculating debts/balance for a chosen participant |
      | Bas | Implement tag system for expense (color, remove/edit/add tags) |
      | Minh | Continue with refining UI Design, design Open Debts page |
      |Jochem|Create a log of payments which is used for calculating debts |
      |Jakub| Implement long-polling|
      |Everyone| UI Testing and logic testing|
