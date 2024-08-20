# Meeting Week 4

 Key | Value |
| --- | --- |
| Date: | 05-03-2024 |
| Time: | 13:45 |
| Location: | DW Project Room 4 |
| Chair | Jakub Florek |
| Minute Taker | Jochem Kralt |
| Attendees | Rares Patrut, Bas Bruijnis, Nguyen Cao Minh, Kiarash Karimi |


### Agenda items:
- **Opening by chair (1 min)**
- **Check-in: How is everyone doing? (3 min)**
- **Approval of the agenda - Does anyone have any additions? (1 min)**
<br>No additions

- **Announcements by the TA**
<br>Question: are there conflicts with any of the presentation time slots in week 10? Not yet.

- **Talking Points: (Inform/ brainstorm/ decision making/ discuss)**
  - **What are the impressions after last week's tasks. Is everyone finished? What is the state of the endpoint implementation? (2 min)**
  - **Present the current progress to the TA (3 min)**
  <br>
- backend progress, data model 
- language picker
- mailing functionality
- communication with server
- invitation merged & invite code functionality<br>
  - **Discuss a way to implement the expense splitting. Discuss the somewhat ambigous requirement of N - 1 payments. (2 min)**
<br> We should ask Sebastian for a more elaborate explanation of this requirement. Rares illustrated the ways of expense splitting.
TA explained that when combining multiple payments from X to Y, you're already minimizing the payments from the perspective of X to N-1 payments maximum.<br>
  - **Discuss saving all the viewed Events and previewing them without joining (2 min)**
<br>We want the recently viewed events window to contain a history of events the user viewed. There are no user accounts so this will not be related to the user's events. We want the most recently opened events to be at the top of the list.<br>
  - **Discuss Buddycheck 1 assignment (deadline on Friday) (3 min)**
<br>We should all fill out the form sent by email. The TA mentioned to pay attention to the AID model when providing feedback, also described in the rubric.<br>
  - **Discuss Task & Planning assignment (deadline on Friday) (5 min)**
<br>Tasks & Planning will be our focus this week: we want to structure the tasks and divide the issues in GitLab. A rule proposed by Bas: we only want to work on issues, anything else you want to work on should be made an issue.
The TA proposed that we should make weekly milestones, and create + divide issues after every meeting. We should also add time estimations to every issue (also explained in rubric).<br>
  - **Decide the progress for next week (10 min)**  
     - **Connecting UI to the database**

      | Screen       | Assignee |
      |--------------|----------|
      | Start        | Jakub    |
      | Overview     | Rares    |
      | Invitation   | Bas      |
      | Expense      | Kiarash  |
      | Participants | Minh     |
      | Open Debts   | Jochem   |

    - **Cleaning up the template project files**
  <br>This will be postponed to a later week.<br>
    - **Removing mock parts of code introduced in week 2 due to the lack of data model**
    - **Split the tasks and set milestones**
  - **Discuss the exam week (10 min)**  
    - **Testing and adding issues**
<br> This will be our main focus, we want to slow down on features and create a better view of what we want to achieve in the second half of the course.<br>
    - **Code quality and refactoring**
<br> This will also be part of our main focus: cleaning up our code and improving the quality (over the quantity).<br>
    - **New features and client-side logic**
<br> This will have less attention, but this can be discussed during our extra meeting on Thursday.<br>
    - **Team bonding?**
<br> We will go bowling on Thursday :)<br>
- **Summarize action points: Who , what , when? (2 min)**
<br>
- See division of the views (connection to database)
- In case of not enough contributions: refactor code and improve the quality
- Create milestone and issues after this meeting and divide them
- **Feedback round: What went well and what can be improved next time? (1 min)**
<br> We all thought the meeting went well<br>
- **Planned meeting duration != actual duration? Where/why did you mis -estimate? (1 min)**
<br> We had enough time to discuss all the points and did not have time left.<br>
- **Question round: Does anyone have anything to add before the meeting closes? (1 min)**
- **Closure (1 min)**
