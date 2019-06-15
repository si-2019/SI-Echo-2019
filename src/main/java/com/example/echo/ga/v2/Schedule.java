package com.example.echo.ga.v2;

import com.example.echo.ga.v2.Domain.Class;
import com.example.echo.ga.v2.Domain.Department;
import com.example.echo.ga.v2.Domain.MeetingTime;
import com.example.echo.ga.v2.Domain.Professor;

import java.util.ArrayList;

public class Schedule {
	private ArrayList<Class> classes;
	private boolean isFitnessChanged = true;
	private double fitness = -1;
	private int classNumb = 0;
	private int numbOfConflicts = 0;
	private Data data;
	public Data getData() { return data; }
	public Schedule(Data data) {
		this.data = data;
		classes = new ArrayList<Class>(data.getNumberOfClasses());
	}
	public Schedule initialize() {

		new ArrayList<Department>(data.getDepts()).forEach(dept -> {
				data.getStudentsGroups().forEach(sg -> {
					sg.getCourses().forEach(course -> {
						Class newClass = new Class(classNumb++, dept, course); //ne treba
						ArrayList<MeetingTime> mtRandom = new ArrayList<>();
						for(MeetingTime mt : data.getMeetingTimes()) {
							course.getProfessors().forEach(p -> {
								if(p.getId() == mt.getIdProf() && p.isAssistent() == sg.isLabGroup())
									mtRandom.add(mt);
							});
						}

						newClass.setMeetingTime(mtRandom.get((int) (mtRandom.size() * Math.random())));
						newClass.setStudentsGroup(sg);
						newClass.setRoom(data.getRooms().get((int) (data.getRooms().size() * Math.random())));

						ArrayList<Professor> profRandom = new ArrayList<>();
						for(Professor p: course.getProfessors()) {
							if(p.isAssistent() == sg.isLabGroup())
								profRandom.add(p);
						}
						newClass.setProfessor(profRandom.get((int) (profRandom.size() * Math.random())));
						classes.add(newClass);
				});
			});
		});
		return this;
	}
	public int getNumbOfConflicts() { return numbOfConflicts; }
	public ArrayList<Class> getClasses() {
		isFitnessChanged = true;
		return classes; 
	}
	public double getFitness() { 
		if (isFitnessChanged == true) {
			fitness = calculateFitness();
        	isFitnessChanged = false;
		}
		return fitness; 
	}
	private double calculateFitness() {
		numbOfConflicts = 0;
		classes.forEach(x -> {
			if (x.getRoom().getSeatingCapacity() < x.getStudentsGroup().getNumberOfStudents()) numbOfConflicts++;
			if(x.getRoom().isLab() != x.getStudentsGroup().isLabGroup()) numbOfConflicts++;
			classes.stream().filter(y -> classes.indexOf(y) >= classes.indexOf(x)).forEach(y -> {
				if (x.getMeetingTime().getDay() == y.getMeetingTime().getDay()
						&& x.getMeetingTime().getTime() == y.getMeetingTime().getTime()
						&& x.getId() != y.getId()) {
					if (x.getRoom() == y.getRoom()) numbOfConflicts++;
                    if (x.getProfessor() == y.getProfessor()) numbOfConflicts++;
				}
			});
		});
		return 1/(double)(numbOfConflicts + 1);
	}
	public String toString() {
		String returnValue = new String();
		for (int x = 0; x < classes.size()-1; x++) returnValue += classes.get(x) + ",";
		returnValue += classes.get(classes.size()-1);
		return returnValue;
	}
}
