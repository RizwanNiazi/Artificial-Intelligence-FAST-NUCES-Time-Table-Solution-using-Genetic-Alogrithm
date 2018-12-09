package ai.project;

import java.util.ArrayList;

public class Population {
        
    ArrayList<Timetable> timetables;
    int populationSize = 200;

    public Population() 
    {
        timetables = new ArrayList<>(populationSize);
        for(int i = 0; i < populationSize;i++)
        {
            Timetable timetable = new Timetable();
            timetable.genTimetable(University.slots);
            timetables.add(timetable);
        }
    }
    
    public Timetable getFittest() 
    {
        int maxFit = -10000;
        int maxFitIndex = 0;
        for (int i = 0; i < timetables.size() ; i++) {
            if (maxFit <= timetables.get(i).fitness) {
                maxFit = timetables.get(i).fitness;
                maxFitIndex = i;
            }
        }
        return timetables.get(maxFitIndex);
    }
    
    public Timetable getSecondFittest() 
    {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < timetables.size(); i++) {
            if (timetables.get(i).fitness > timetables.get(maxFit1).fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (timetables.get(i).fitness > timetables.get(maxFit2).fitness) {
                maxFit2 = i;
            }
        }
        return timetables.get(maxFit2);
    }
    
    public Timetable getLeastFittest()
    {
        int minFit = 10000;
        int minFitIndex = 0;
        for (int i = 0; i < timetables.size(); i++) 
        {
            if (timetables.get(i).fitness <= minFit) 
            {
                minFit = timetables.get(i).fitness;
                minFitIndex = i;
            }
        }
        return timetables.get(minFitIndex);
    }
    
}
