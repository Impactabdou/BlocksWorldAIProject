package modelling;

import java.util.*;

public class BooleanVariable extends Variable {
	public BooleanVariable(String name) {
		super(name, new HashSet<Object>(Arrays.asList(true, false)));
	}
}
