package de.terrestris.shogun.hibernatecriteria.filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import de.terrestris.shogun.dao.DatabaseDao;

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
	public Criterion makeCriterion(Class mainClass) {

		Criterion criterion = null;

		int operandCount = this.getOperands().size();

		String fieldName = this.getFieldName();
		String originalFieldName = fieldName;
		
		// when we were called with a fieldname that contains a dot,
		// we have to change the mainClass and the fieldanem
		if (fieldName != null && fieldName.contains(".")) {
			String[] parts = fieldName.split("\\.");
			Class clz = this.getClassFromFieldName(parts[0], mainClass);
			mainClass = clz;
			fieldName = parts[1];
		}
		
		
		Object finalOperand = null;
		Object finalOperand2 = null;
		
		if (this.getOperator() != Operator.Statement) {
			// Cast the operands to the correct data type of the DB field
			finalOperand = castOperandToFinalDataType(mainClass, fieldName, this.getOperands().get(0).getOperand().toString());
			
			finalOperand2 = null;

			if (operandCount == 2) {
				finalOperand2 = castOperandToFinalDataType(mainClass, fieldName, this.getOperands().get(1).getOperand().toString());
			}
		} else {
			finalOperand = (String) this.getOperands().get(0).getOperand();
		}
		
		switch (this.getOperator()) {
		case Any:
			break;
		case Between:
			if (operandCount == 2) {
				criterion = Restrictions.between(originalFieldName, finalOperand, finalOperand2);
			}
			break;
		case Equals:
			criterion = Restrictions.eq(originalFieldName, finalOperand);
			break;
		case Greater:
			criterion = Restrictions.gt(originalFieldName, finalOperand);
			break;
		case GreaterEq:
			criterion = Restrictions.ge(originalFieldName, finalOperand);
			break;
		case In:
			Object[] values = new Object[operandCount];
			for (int o = 0; o < operandCount; o++) {
				values[o] = finalOperand;
			}
			criterion = Restrictions.in(originalFieldName, values);
			break;
		case IsNull:
			criterion = Restrictions.isNull(originalFieldName);
			break;
		case Like:
			criterion = Restrictions.like(originalFieldName, finalOperand);
			break;
		case ILike:
			//TODO remove the ternary operator
			// introduce exclude fields in client
			criterion = (finalOperand != null) ? Restrictions.ilike(originalFieldName, finalOperand) : null;
			break;
		case NotEquals:
			criterion = Restrictions.ne(originalFieldName, finalOperand);
			break;
		case NotLike:
			criterion = Restrictions.not(Restrictions.like(originalFieldName, finalOperand));
			break;
		case NotNull:
			criterion = Restrictions.isNotNull(originalFieldName);
			break;
		case Smaller:
			criterion = Restrictions.lt(originalFieldName, finalOperand);
			break;
		case SmallerEq:
			criterion = Restrictions.le(originalFieldName, finalOperand);
			break;
		case Statement:
			criterion = Restrictions.sqlRestriction((String)finalOperand);
			break;
		case DWithin:
			criterion = SpatialRestrictions.within(originalFieldName, (Geometry)finalOperand);
			break;
		}
		return criterion;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param fieldName
	 * @param mainClass
	 * @return
	 */
	private Class getClassFromFieldName(String fieldName, Class mainClass) {
		Class c = null;
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
					c = (Class) ttt;
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
	private Object castOperandToFinalDataType(Class mainClass, String fieldName, String operand) {
		Object finalOperand = null;
		List<Field> fieldList = new ArrayList<Field>();
		fieldList = this.getAllFields(fieldList, mainClass);
		
		for (int j = 0, m = fieldList.size(); j < m; j++) {
			
			Field field = fieldList.get(j);
			
			if (field.getName().equals(fieldName)) {
				// Class Integer
				if (field.getType().equals(java.lang.Integer.class)) {
					finalOperand = new Integer(operand);
				}
				// data type int
				else if (field.getType().toString().equals("int")) {
					finalOperand = new Integer(operand);
				} 
				
				// Class Double
				else if (field.getType().equals(java.lang.Double.class)) {
					finalOperand = new Double(operand);
				}
				// data type double
				else if (field.getType().toString().equals("double")) {
					finalOperand = new Double(operand);
				} 
				
				// data type boolean
				else if (field.getType().toString().equals("boolean")) {
					finalOperand = new Boolean(operand);

					// data type DATE
				} else if (field.getType().equals(java.util.Date.class)) {

					try {
						finalOperand = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						finalOperand = formatter.parse(operand);
						
					} catch (ParseException e) {
						LOGGER.error("ParseException in hibernate filteritem for Date: " + operand, e);
					}
				
				}	// data type GEOMETRY POINT, etc... 
				else if (field.getType().equals(com.vividsolutions.jts.geom.Point.class) || 
							field.getType().equals(com.vividsolutions.jts.geom.Geometry.class)) {
					
					try {
						
						Geometry point = new WKTReader().read((String)this.getOperands().get(0).getOperand());
						
						// we receive null for a string like %titt% although we catch ParseException
						if (point != null) {
							
							//TODO: set the SRID dynamically
							point.setSRID(900913);
							double distance = Double.parseDouble((String)this.getOperands().get(1).getOperand());
							Geometry g = point.buffer(distance);
							//TODO: set the SRID dynamically
							g.setSRID(900913);
							
							finalOperand = g;
						}
						
	
					} catch (com.vividsolutions.jts.io.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				else if (false) {
					// ... more types here
				}
				// DEFAULT is String
				else {
					finalOperand = operand;
				}
				
				// found a datatype
				break;
			}

		}
		
		if (finalOperand == null) {
			LOGGER.warn("Failed to determine the final data-type of field " +
				fieldName + " in class " + mainClass.getSimpleName() +
				" for operand " + operand + ". Falling back to datatype java.lang.String.");
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
