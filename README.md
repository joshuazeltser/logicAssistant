# LogicAssistant

## Introduction
Mathematical logic is an area used throughout the engineering and scientific industries. Whether its developing artificial intelligence software or students completing a Computer Science degree, logic is a fundamental tool. In order to ensure that logic is used correctly a proof system must be used. Natural Deduction provides the tools needed to deduce and prove the validity of logical problems, making it a vital tool for everyone to learn to use. This is why many universities make it a priority to teach this to their students as they begin their studies. For students new to Natural Deduction or even those more advanced users are often left stuck in the middle of a proof not knowing what to do next, and then when they have completed the proof are unsure as to whether it is valid. LogicAssistant is being created to assist users with this problem.

## Problem
Although the basics of Natural Deduction are easy to learn by just committing to memory the various rules that are defined, when it comes to more complex proofs it can be difficult to work out whether your proof is correct. You may have made an assumption at some point in your proof that was invalid, or just mistakenly used the wrong rule. It is not always the case that you can find someone who is an expert in this field who can check over your proof for you. This problem assumes that you are even able to get to the point of completing your proof. Often you may just have to sit staring at a certain point in your proof unsure of what to do next. You could at this stage go through each rule and try to work out which fits best, but this is quite a short-sighted approach as you may be going off in the complete wrong direction. This is also quite time consuming. When using Natural Deduction in the real world there is not always someone around to point out which rule to use next. 

## Solution
In order to remedy these problems I am building LogicAssistant which will have a number of useful features that will aid users with their Natural Deduction proofs. The first feature which I aim to create is a proof checker which allows the user to type in their Natural Deduction Proof specifying all of the rules they have used, and the software will notify them whether it is valid. I also want the user to be notified which parts of the proof are wrong and are thereby causing the proof to be invalid. This means that a user will never be unsure whether a proof they have created is valid and if it isn't they will know immediately where they went wrong. The other main feature that LogicAssistant will have is the ability to ask for hints at any stage of a proof. This means that if a user is stuck at any point in their proof they can ask the software to provide them with the next rule to apply. This will be very helpful especially for students who are new to Natural Deduction proof techniques. All of these features and more will be condensed together into an easy to use web interface that a user can access whenever they need to check or solve a proof. 

There are tools already out there that assist you with Natural Deduction. Most of these tools are very complex and difficult to use, requiring a high technical and mathematical knowledge to understand them. This makes them often a waste of time to use on a simple Natural Deduction proof. There are also tools like Pandora\cite{pandora} which allows you to enter your Natural Deduction proof step by step and tells you whether it is valid. The drawback of this software is that it only allows you to enter a step in the proof if it is valid. This means that the amount that can be learnt from this tool is limited as through trial and error you could eventually solve your proof without really knowing what you are doing. The motivation of LogicAssistant is to fill in these gaps left by existing solvers to help solve Natural Deduction proofs.
