# Notes Meeting Week 3

|  Key         | Value                                                            |
|--------------|------------------------------------------------------------------|
| Date:        | 27-02-2024                                                       |
| Time:        | 13:45                                                            |
| Location:    | DW Project Room 4                                                |
| Chair        | Raresh Patrut                                                    |
| Minute Taker | Bas Bruijnis                                                     |
| Attendees    | Jakub Florek, Nguyen Cao Minh, Kiarash Karimi ~~, Jochem Kralt~~ |


### Agenda items:
- Opening by chair (1 min)
- Check-in: How is everyone doing? (3 min)
- Approval of the agenda - Does anyone have any additions? (1 min)
#### No additions
- Announcements by the TA 
#### No announcements
- **Talking Points:** (Inform/ brainstorm/ decision making/ discuss)
  - How is everyone feeling about the past week's progress (short opinion about the task - difficulty, time-spent etc) ~ (4 min)

####  Last week there were quite some new things to learn (e.g. JavaFX), which was a bit difficult but also educational, everybody was satisfied with the progress, there was good and quick teamwork, we are happy with the communication on whatsapp and gitlab
  - Present the current progress to the TA ~ (7 min)
  
#### The TA was also very happy with the progress
#### The invitation page was not merged yet, due to Jochem's sickness and holiday
#### During the presentation the team was not quite sure where the "addeditparticipants" view would end up in the application, the discussion of this was postponed due to the full agenda.
  
  

  - Discussion on Code of Conduct ~ (10 min)
    - Has everyone managed to finish their part
    
    #### Everybody finished their assigned sections (either in full or a sketch)
    #### However not everybody put it in overleaf yet, as was discussed in last weeks meeting
  
    #### The team decided to pull the COC deadline forward to thursday 14pm. By this time the sections should be complete and on overleaf, so that the others can review.
  
    - Take a quick look and make suggestion about each chapter
    
    #### It was briefly mentioned that there exist somewhat duplicate sections in the current version of the COC, this was not solved during the meeting, nor was there made a clear agreement of how to prevent/solve this.
  
    - Discuss the final format and how are we going to put it together
    > [Code of Conduct outline Google Docs](https://docs.google.com/document/d/1U_-e0qCN4i49hxyZQJMtfAMExv9bpZWgJykYFGjRgdk/edit)
    
    > [Code of Conduct on Overleaf](https://www.overleaf.com/8411524544rybtxffgtxtq#2e46e0)
   
  #### We will put the final version together on Overleaf (mandatory), an abstract was already written last week and the order of the COC will be the same as in the assignment document.

  - Discuss the formating of the code, i.e. auto-formatting settings (IDE settings and checkstyle) (3 min)
  
  #### The team decided to use the OOP checkstyle file for consistent codestyle across all members.
  #### Import and modify (if necessary) it today(!) (+ reference where we got it from)

  - Decide the progress for next week (10 min)
  
  #### It was proposed to try to link this weeks tasks to what is discussed in the lectures, Minh noted that this week we will learn about backend implementation, so this week we will do Data Modeling and API endpoints.
  
- Bring suggestions about future features that should be implemented (database, APIs etc)

#### We decided to discuss the  datastructures and DB design: start with the biggest entity, which is most probably the event:
  ##### Event contains a list of participants(ID, name, email, iban, bic)
  ##### Event contains a list of expenses (ID, creditor, debtors, title, date, amount, currency, type (label))
  ##### From the expenses the balances (who owes who) of everybody can be derived, using also a payment class (created when a payment is actually made)
  ##### Event contains a list of payments, Payment:(ID, payer, receiver, amount, currency, description, date)
  ##### Event contains an invitecode attribute (ID), and name


### Discussed for future implementations:
#### [DIFFERENT FEATURE] For the future we will most probably use a JSON file for each language setting, the files have the same IDs with the translation for the respective language

#### [DIFFERENT FUNCTIONALITY] We considered how to keep track of the users without creating user accounts (not allowed by assignment):
#### We will firstly locally store an array (or JSON file) containing the event IDs (and possibly names) which the user visited recently
#### Additionally, when a user opens the event overview, a window will pop-up with all the participants already in the event and it will prompt the user with the question if they are already in the list, if they are not, they can add themselves (add name, email, bank details)

  - Split the tasks and set a deadline

#### After the planned meeting another 'emergency' meeting was planned, here the design of all the datastructures were finished (some where even implemented), the checkstyle was also configured.
### Tasks:
#### Bas will look at the email functionality + (hashcode, equals, tostring) of event (copy/paste)
#### Rares will change the panel on the add-edit-expense page to an alert + payment endpoints
#### Jakub: Event endpoints
#### Jochem: get better soon + merge invitation page, Polish UI (+ Statistics)
#### Minh: Participants endpoints
#### Kiarash: Expense endpoints

  - Discuss **merging** criterias (2 min)
  
#### Should only merge your own merge requests, and only after approvals.
#### Divide weekly into two groups who should (not mandatory but strongly encouraged) approve their groups merge requests, can still optionally comment and approve on other groups merge requests (should keep your merge request open for a while so that the other group can check it).
#### The reason we decided to do this is so that you don't need to wait for the complete team to approve your request
#### This week:
#### Group 1: Bas, Rares, Jochem
#### Group 2: Jakub, Minh, Kiarash

### Summarize action points: Who , what , when? (2 min)

#### Already added the checkstyle from OOP today
#### Code Of Conduct should be done at thursday 14:00 (official deadline: friday)
#### Bas will look at the email functionality + (hashcode, equals, tostring) of event (copy/paste)
#### Rares will change the panel on the add-edit-expense page to an alert + payment endpoints
#### Jakub: Event endpoints
#### Jochem: get better soon + merge invitation page, Polish UI (+ Statistics)
#### Minh: Participants endpoints
#### Kiarash: Expense endpoints

- Feedback round: What went well and what can be improved next time? (1 min)

#### Everybody was content with the meeting

- Planned meeting duration != actual duration? Where/why did you mis -estimate? (1 min)

#### Not actually long enough because another meeting was added after this one to discuss the data design and start on it,
#### The reason that the meeting was to short is because some uncertainties came up about the location of the views and also the data modeling took a while. These points weren't discussed or thought of before, because we did not know what we would be working on this week

- Question round: Does anyone have anything to add before the meeting closes? (1 min)
- Closure (1 min)

#### The TA is happy, emphasizes the importance of DB design and that she is happy we are already discussing it
