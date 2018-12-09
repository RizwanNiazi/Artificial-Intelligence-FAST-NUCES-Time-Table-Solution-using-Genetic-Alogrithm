package ai.project;

import java.util.ArrayList;
import java.util.Random;

public class University {
    //attributes to create timetable
    static ArrayList<String> slots;
    static int slotsPerDay = 5;
    static int classrooms = 4;
    static int days = 5;
    
    //attributes to create teacher-course-section combination
    int courses = 16;
    int teachers = 8;
    int sections = 4;
    

    public University() 
    {
        int size = courses * sections; //total number of sections each course has.
        slots = new ArrayList<>(size); //each slot will have a teachers, and a course section
        this.getClasses();
    }
    
    private void getClasses()
    {
        String mCourse = Integer.toBinaryString(courses);
        String mTeacher = Integer.toBinaryString(teachers);
        String mSection = Integer.toBinaryString(sections);
        int t = 0;
        String mClass = "";
        for(int i = 0; i < courses; i++)
        {
            mClass = "";
            String teacher = String.format("%3s", Integer.toBinaryString(t)).replace(" ", "0");
            for(int j = 0; j < sections; j++)
            {
                String course = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                String section = String.format("%2s", Integer.toBinaryString(j)).replace(" ", "0");
                mClass = teacher + course + section;
                slots.add(mClass);
            }
            if(i % 2 == 1)
            {
                if (i != 0) {
                    t++;
                }
            }
        }
    }
    
    // the function that creates the fittest timetable
    public void GeneticAlgorithm()
    {
        Random rand = new Random();
        
        double mutationRate = 0.2;
        double mutation;
        int mutateAtIndex;
        int iterations = 50;
        
        Population population = new Population();
        
        Timetable fittest = new Timetable();
        Timetable secondFittest = new Timetable();
        
        Children children = new Children();
        
        fittest = population.getFittest();
        secondFittest = population.getSecondFittest();
        
        while(0 < iterations)
        {
            //crossover function
            children = getChildren(fittest, secondFittest);
            
            Timetable child_1 = new Timetable();
            child_1 = children.child_1;
            
            Timetable child_2 = new Timetable();
            child_2 = children.child_2;
            
            Timetable leastFittest = new Timetable();
            leastFittest = population.getLeastFittest();
            
            if(leastFittest.fitness < child_1.fitness )
            {
                population.timetables.remove(leastFittest);
                population.timetables.add(child_1);
            }
            
            leastFittest = population.getLeastFittest();
            if(leastFittest.fitness < child_2.fitness )
            {
                population.timetables.remove(leastFittest);
                population.timetables.add(child_2);
            }
            
            mutateAtIndex = rand.nextInt(classrooms * slotsPerDay * days) + 0;
            mutation = (rand.nextInt(100) + 0) * 0.01;
            if(mutation < mutationRate)
            {
                population.timetables.get(mutateAtIndex).mutate();
            }
            fittest = population.getFittest();
            System.out.println(fittest.timetable + "\n"+fittest.fitness);
            iterations--;
        }
        System.out.println(fittest.timetable + "\n"+fittest.fitness);
        System.out.println(getTimetableArrayList(fittest));
    }
    
    public Children getChildren(Timetable parent_1, Timetable parent_2)
    {
        Children children = new Children();
        int i = 0;
        Random rand = new Random();
        int crossOverType = rand.nextInt(2) + 1;
        char ch;
        
        //this is a one cut cross
        if(crossOverType == 1)
        {
            rand = new Random();
            int indexToCutAt = (rand.nextInt(classrooms * slotsPerDay * days) + 0) * 9;
            
            for (i = 0; i < indexToCutAt; i++)
            {
                ch = parent_1.timetable.charAt(i);
                children.child_1.timetable = children.child_1.timetable.concat(Character.toString(ch));
            
                ch = parent_2.timetable.charAt(i);
                children.child_2.timetable = children.child_2.timetable.concat(Character.toString(ch));
            }
            for (i = indexToCutAt; i < classrooms * slotsPerDay * days * 9; i++)
            {
                ch = parent_2.timetable.charAt(i);
                children.child_1.timetable = children.child_1.timetable.concat(Character.toString(ch));
                
                ch = parent_1.timetable.charAt(i);
                children.child_2.timetable = children.child_2.timetable.concat(Character.toString(ch));
            }
            
            children.child_1.getFitness();
            children.child_2.getFitness();
        }
        
        //this is a two cut cross
        else
        {
            int inc = classrooms * slotsPerDay * days;
            int indexToCutAt_1 = (rand.nextInt((inc/2)) + 0) * 9;
            int indexToCutAt_2 = (rand.nextInt(inc/2) + inc/2) * 9;
            
            for (i = 0; i < indexToCutAt_1; i++)
            {
                ch = parent_1.timetable.charAt(i);
                children.child_1.timetable = children.child_1.timetable.concat(Character.toString(ch));
            
                ch = parent_2.timetable.charAt(i);
                children.child_2.timetable = children.child_2.timetable.concat(Character.toString(ch));
            }
            
            for (i = indexToCutAt_1;i<indexToCutAt_2;i++)
            {
                ch = parent_2.timetable.charAt(i);
                children.child_1.timetable = children.child_1.timetable.concat(Character.toString(ch));
                
                ch = parent_1.timetable.charAt(i);
                children.child_2.timetable = children.child_2.timetable.concat(Character.toString(ch));
            }
            
            for (i = indexToCutAt_2; i < classrooms * slotsPerDay * days * 9; i++)
            {
                ch = parent_1.timetable.charAt(i);
                children.child_1.timetable = children.child_1.timetable.concat(Character.toString(ch));
            
                ch = parent_2.timetable.charAt(i);
                children.child_2.timetable = children.child_2.timetable.concat(Character.toString(ch));
            }
            children.child_1.getFitness();
            children.child_2.getFitness();           
        }
        return children;
    }
    
    public ArrayList<String> getTimetableArrayList(Timetable timetable)
    {
        ArrayList<String> mtimetable = new ArrayList<>(classrooms * slotsPerDay * days );
        String timeSlot = "";
        int k , j;
        for( int i = 0; i < classrooms * slotsPerDay * days; i++ )
        {
            timeSlot = "";
            for( j = i*9, k = i*9; j < k+9 ; j++ )
            {
                timeSlot = timeSlot.concat(Character.toString(timetable.timetable.charAt(j)));
                
            }
            mtimetable.add(timeSlot);
        }
        return mtimetable;
    }
}
