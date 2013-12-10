package de.terrestris.shogun.hibernatecriteria.filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import de.terrestris.shogun.dao.DatabaseDao;
import de.terrestris.shogun.hibernatecriteria.filter.FilterItem;

/**
 * Class for a Hibernate filter condition
 * 
 * @author terrestris GmbH & Co. KG
 * 
 */
public class HibernateFilterItem extends FilterItem {
	
	/**
	 * 
	 */
	private static Logger LOGGER = Logger.getLogger(HibernateFilterItem.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7799174921527642750L;

	/**
	 * Creates a Hibernate criterion by filter conditions
	 * 
	 * @return the hibernate criterion object
	 */
	public Criterion makeCriterion(Class<?> mainClass) {

		Criterion criterion = null;

		Class<?> originalGivenClass = mainClass;
		Operator operator = this.getOperator();

		String fieldName = this.getFieldName();
		String originalFieldName = fieldName;
		
		boolean fieldNameContainsDot = (fieldName != null && fieldName.contains("."));
		
		// when we were called with a fieldname that contains a dot,
		// we have to change the mainClass and the fieldname
		if (fieldNameContainsDot) {
			String[] parts = fieldName.split("\\.");
			Class<?> clz = this.getClassFromFieldName(parts[0], mainClass);
			mainClass = clz;
			fieldName = parts[1];
		}
		
		String debugMsg = "Will create a '" + operator + "'-criterion" +
				" for class '" + mainClass.getSimpleName() + "', field '" + fieldName + "'.";
		
		if (fieldNameContainsDot) {
			debugMsg += " These values were determined from a fieldname" +
				" with a dot in it. Originally we were passed '" + 
				originalFieldName + "' as" + " fieldname and '" + 
				originalGivenClass.getSimpleName() + "' as class.";
		}
		LOGGER.debug(debugMsg);
		
		
		int operandCount = this.getOperands().size();
		
		if (operandCount == 0) {
			LOGGER.debug("Didn't receive any operands to convert. This might" +
					" lead to problems if the used operator" +
					" '" + operator + "' needs operands and" +
					" not only the fieldname.");
		} else {
			String operandDsp  = "operand" + ((operandCount == 1) ? "" : "s");
			String needDsp  = "need" + ((operandCount != 1) ? "" : "s");
			LOGGER.debug("Received " + operandCount + " " + operandDsp +
					" which probably " + needDsp + " conversion.");
		}
		
		// Create an Object array with all operands casted to the final datatype
		Object[] operandsObj = new Object[operandCount];
		if (operator != Operator.Statement) {
			for (int i = 0; i < operandCount; i++) {
				String strOperand = this.getOperands().get(i).getOperand().toString();
				Object objOperand = castOperandToFinalDataType(mainClass, fieldName, strOperand);
				operandsObj[i] = objOperand;
			}
		} else {
			LOGGER.debug("The operands of the " + Operator.Statement + " will" +
					" not be casted.");
			if(operandCount > 0) {
				operandsObj[0] = this.getOperands().get(0).getOperand().toString();
			}
		}
		
		// Since we most often use either one or two operands, save aliases
		Object finalOperand1 = null;
		Object finalOperand2 = null;
		
		if (operandCount >= 1) {
			finalOperand1 = operandsObj[0];
			if (operandCount >= 2) {
				finalOperand2 = operandsObj[0];
			}
		}
		
		switch (operator) {
		case Any:
			List<Object[]> dividedOperands = this.divideObjectArray(operandsObj, 1000);
			Disjunction disj = Restrictions.disjunction();
			for (Object[] dividedOperand : dividedOperands) {
				Criterion crit = Restrictions.eq(originalFieldName, dividedOperand);
				disj.add(crit);
			}
			criterion = disj;
			break;
		case Between:
			if (operandCount == 2) {
				criterion = Restrictions.between(originalFieldName, finalOperand1, finalOperand2);
			}
			break;
		case Equals:
			criterion = Restrictions.eq(originalFieldName, finalOperand1);
			break;
		case Greater:
			criterion = Restrictions.gt(originalFieldName, finalOperand1);
			break;
		case GreaterEq:
			criterion = Restrictions.ge(originalFieldName, finalOperand1);
			break;
		case In:
			List<Object[]> dividedOperands1 = this.divideObjectArray(operandsObj, 1000);
			
			Conjunction conj1 = Restrictions.conjunction();
			for (Object[] dividedOperand : dividedOperands1) {
				Criterion crit = Restrictions.in(originalFieldName, dividedOperand);
				conj1.add(crit);
			}
			criterion = conj1;
			break;
		case NotIn:
			List<Object[]> dividedOperands2 = this.divideObjectArray(operandsObj, 1000);
			
			Conjunction conj2 = Restrictions.conjunction();
			for (Object[] dividedOperand : dividedOperands2) {
				Criterion crit = Restrictions.not(Restrictions.in(originalFieldName, dividedOperand));
				conj2.add(crit);
			}
			criterion = conj2;
			break;
		case IsNull:
			criterion = Restrictions.isNull(originalFieldName);
			break;
		case Like:
			criterion = Restrictions.like(originalFieldName, finalOperand1);
			break;
		case ILike:
			if (finalOperand1 != null) {
				criterion =  Restrictions.ilike(originalFieldName, finalOperand1);
			}
			break;
		case NotEquals:
			criterion = Restrictions.ne(originalFieldName, finalOperand1);
			break;
		case NotLike:
			criterion = Restrictions.not(Restrictions.like(originalFieldName, finalOperand1));
			break;
		case NotNull:
			criterion = Restrictions.isNotNull(originalFieldName);
			break;
		case Smaller:
			criterion = Restrictions.lt(originalFieldName, finalOperand1);
			break;
		case SmallerEq:
			criterion = Restrictions.le(originalFieldName, finalOperand1);
			break;
		case Statement:
			criterion = Restrictions.sqlRestriction((String)finalOperand1);
			break;
		case DWithin:
			criterion = SpatialRestrictions.within(originalFieldName, (Geometry)finalOperand1);
			break;
		}
		return criterion;
	}
	
	/**
	 * Divides a given Object array into a number of Object arrays where the
	 * length of the smaller chunks never exceeded the given maxCnt. Will return
	 * the created chunks as a List.
	 * 
	 * @param inObj
	 * @param maxCnt
	 * @return
	 */
	private List<Object[]> divideObjectArray(Object[] inObj, int maxCnt) {
		List<Object[]> dividedList = new ArrayList<Object[]>();
		final int inLength = inObj.length;

		// return early for small Object arrays.
		if (inLength <= maxCnt){
			dividedList.add(inObj);
			return dividedList;
		}

		final int numSubArrays = (int) Math.ceil(((double) inLength) / ((double) maxCnt));
		int start = 0;
		
		for (int i = 0; i < numSubArrays; i++) {
			int end = (i + 1) * maxCnt;
			if (end > inLength) {
				end =  inLength;
			}
			Object[] subArr = Arrays.copyOfRange(inObj, start, end);
			dividedList.add(subArr);
			start = end;
		}
		return dividedList;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @param fieldName
	 * @param mainClass
	 * @return
	 */
	private Class<?> getClassFromFieldName(String fieldName, Class<?> mainClass) {
		Class<?> c = null;
		try {
			List<Field> allFields = getAllFields(new ArrayList<Field>(), mainClass);
			Field f = null;
			for (int i = 0; i < allFields.size(); i++) {
				if (allFields.get(i).getName().equals(fieldName)) {
					f = allFields.get(i);
					break;
				}
			}
			
			Type t = f.getGenericType();
			
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				for (Type ttt : pt.getActualTypeArguments()) {
					c = (Class<?>) ttt;
					break;
				}
			}
			
		} catch (SecurityException e) {
			LOGGER.warn("Failed to determine the ParameterizedType of Set " +
					fieldName + " in class " + mainClass.getSimpleName());
		}
		
		return c;
	}

	/**
	 * Cast the operand to the correct data type of the model (DB)
	 * 
	 * @param mainClass
	 * @param fieldName
	 * @param operand
	 * @return
	 */
	private Object castOperandToFinalDataType(Class<?> mainClass, String fieldName, String operand) {
		Object finalOperand = null;
		List<Field> fieldList = new ArrayList<Field>();
		fieldList = this.getAllFields(fieldList, mainClass);
		
		
		Class<?> jtsPointClass = com.vividsolutions.jts.geom.Point.class;
		Class<?> jtsGeometryClass = com.vividsolutions.jts.geom.Geometry.class;
		
		for (int j = 0, m = fieldList.size(); j < m; j++) {
			
			Field field = fieldList.get(j);
			
			if (field.getName().equals(fieldName)) {
				
				Class<?> fieldType = field.getType();
				String fieldTypeStr = fieldType.toString();
				
				if (fieldType.equals(java.lang.Integer.class)) {
					// Class Integer
					finalOperand = new Integer(operand);
				} else if (fieldTypeStr.equals("int")) {
					// data type int
					finalOperand = new Integer(operand);
				} else if (fieldType.equals(java.lang.Double.class)) {
					// Class Double
					finalOperand = new Double(operand);
				} else if (fieldTypeStr.equals("double")) {
					// data type double
					finalOperand = new Double(operand);
				} else if (fieldType.equals(java.lang.Boolean.class)) {
					// Class Boolean
					finalOperand = new Boolean(operand);
				} else if (fieldTypeStr.equals("boolean")) {
					// data type boolean
					finalOperand = new Boolean(operand);
				} else if (fieldType.equals(java.util.Date.class)) {
					// data type Date
					try {
						finalOperand = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						finalOperand = formatter.parse(operand);
						
					} catch (ParseException e) {
						LOGGER.error("ParseException in hibernate filteritem for Date: " + operand, e);
					}
				} else if (fieldType.equals(jtsPointClass) || fieldType.equals(jtsGeometryClass)) {
					// data type GEOMETRY POINT, etc... 
					try {
						Geometry point = new WKTReader().read((String)this.getOperands().get(0).getOperand());
						
						// we receive null for a string like %titt% although we catch ParseException
						if (point != null) {
							point.setSRID(900913); //TODO: set the SRID dynamically
							double distance = Double.parseDouble((String)this.getOperands().get(1).getOperand());
							Geometry g = point.buffer(distance);
							g.setSRID(900913); //TODO: set the SRID dynamically
							finalOperand = g;
						}
					} catch (com.vividsolutions.jts.io.ParseException e) {
						LOGGER.error("ParseException in hibernate filteritem for Geometry: " + operand, e);
					}
				} else {
					// DEFAULT is String
					finalOperand = operand;
				}
				
				// found a matching field, determined a datatype and casted.
				break;
			}
		}
		
		if (finalOperand == null) {
			LOGGER.warn("Failed to determine the final data-type of field " +
				fieldName + " in class " + mainClass.getSimpleName() +
				" for operand " + operand + ". Falling back to datatype " +
				"java.lang.String. Is this field existing in the class?");
			finalOperand = new String(operand);
		}

		return finalOperand;
	}
	
	/**
	 * TODO we have a very similar method in {@link DatabaseDao}.
	 * 
	 * 
	 * @param fields
	 * @param type
	 * @return
	 */
	public List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    for (Field field: type.getDeclaredFields()) {
	        fields.add(field);
	    }

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
}
