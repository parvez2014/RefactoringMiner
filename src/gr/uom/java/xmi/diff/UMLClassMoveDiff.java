package gr.uom.java.xmi.diff;

import gr.uom.java.xmi.UMLClass;

public class UMLClassMoveDiff extends UMLClassBaseDiff implements Comparable<UMLClassMoveDiff> {
	
	public UMLClassMoveDiff(UMLClass originalClass, UMLClass movedClass) {
		super(originalClass, movedClass);
		processOperations();
		processAttributes();
	}

	public UMLClass getMovedClass() {
		return nextClass;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ");
		sb.append(originalClass.getName());
		sb.append(" was moved to ");
		sb.append(nextClass.getName());
		sb.append("\n");
		return sb.toString();
	}

	public boolean equals(Object o) {
		if(this == o) {
    		return true;
    	}
		
		if(o instanceof UMLClassMoveDiff) {
			UMLClassMoveDiff classMoveDiff = (UMLClassMoveDiff)o;
			return this.originalClass.equals(classMoveDiff.originalClass) && this.nextClass.equals(classMoveDiff.nextClass);
		}
		return false;
	}

	public int compareTo(UMLClassMoveDiff other) {
		return this.originalClass.getName().compareTo(other.originalClass.getName());
	}
}
