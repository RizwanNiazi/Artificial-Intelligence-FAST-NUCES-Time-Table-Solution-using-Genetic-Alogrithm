package ai.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Timetable {
    
    String timetable = "";
    int fitness = 1000;

    public Timetable() 
    {}
    
    public void genTimetable(ArrayList<String> slots)
    {
        int [] array = new int[slots.size()];
        Arrays.fill(array, 2);
        
        Random rand = new Random();
        int index, freeSlot;
        
        for(int i = 0 ; i < (University.days * University.classrooms * University.slotsPerDay) ; i++)
        {
            freeSlot = rand.nextInt((University.days * University.classrooms * University.slotsPerDay) - 1) + 0;
            index = rand.nextInt(slots.size() - 1) + 0;
            if(freeSlot < 50)
            {
                String emptySlot = "$$$$$$$$$";
                timetable += emptySlot;
            }
            else
            {
                if(array[index] == 0)
                {
                    i--;
                }
                else
                {
                    String slot = slots.get(index);
                    timetable += slot;
                    array[index]--;
                }
            }
        }
        fitness = getFitness();
    }
    
    public int getFitness() 
    {
        int [] array = new int[University.slots.size()];
        Arrays.fill(array, 0);
        int j = 0;
        for(int i = 0; i < (University.days * University.classrooms * University.slotsPerDay);i++)
        {
            String str = this.timetable.substring(j, j+9);
            if(University.slots.contains(str))
            {
                int index = University.slots.indexOf(str);
                
                //if the teacher-course-section is appearing more than once 
                //in the same day, give big penalty.
                if(array[index] == 0)
                {
                    array[index] = j;                
                }
                else if(j < ((9 * University.classrooms * University.slotsPerDay) - array[index]) + (9 * University.classrooms * University.slotsPerDay))
                {
                    if(j < ((9 * University.classrooms * University.slotsPerDay) - array[index]))
                    {
                        fitness = fitness - 500;
                        array[index] = j;
                    }
                    else
                    {
                        fitness = fitness - 200;
                        array[index] = j;
                    }
                }                
                //if same teacher is teaching in different classrooms.
                String teacher = str.substring(0, 3);
                int k = (i % 4)* -1;
                for(int l=0;l<University.classrooms;l++)
                {
                    String sameTeacher = timetable.substring((i+k)*9, (i+k)*9 + 3);
                    if(sameTeacher.equals(teacher) && k != 0)
                    {
                        fitness = fitness - 400; 
                    }
                    k++;
                }
                //if same course section is being taught in different classrooms at the same time.
                String course_section = str.substring(3, 9);
                k = (i % 4)* -1;
                for(int l=0;l<University.classrooms;l++)
                {
                    String sameCourse_Section = timetable.substring((i+k)*9 + 3, (i+k)*9 + 9);
                    if(sameCourse_Section.equals(course_section) && k != 0)
                    {
                        fitness = fitness - 400; 
                    }
                    k++;
                }
            }
            j +=9;
        }
        return fitness;
    }

    public void mutate()
    {
        Random rand = new Random();
        int index_1_for_swap = (rand.nextInt(University.slotsPerDay * University.days * University.classrooms) + 0)*9;
        int index_2_for_swap = (rand.nextInt(University.slotsPerDay * University.days * University.classrooms) + 0)*9;
        String newTimetable = this.timetable;
        for( int i = 0 ; i < 5 ; i++)
        {
            newTimetable = newTimetable.replace(newTimetable.charAt(index_1_for_swap), newTimetable.charAt(index_1_for_swap));
            index_1_for_swap++;
            index_2_for_swap++;
        }
        this.fitness = getFitness();
    }
}
