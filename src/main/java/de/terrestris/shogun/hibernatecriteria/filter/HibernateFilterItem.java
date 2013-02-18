package de.terrestris.shogun.hibernatecriteria.filter;

import java.lang.reflect.Field;
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

		Object finalOperand = null;
		Object finalOperand2 = null;
		
		if (this.getOperator() != Operator.Statement) {
			// Cast the operands to the correct data type of the DB field
			finalOperand = castOperandToFinalDataType(mainClass, fieldName, (String) this.getOperands().get(0).getOperand());
			finalOperand2 = null;

			if (operandCount == 2) {
				finalOperand2 = castOperandToFinalDataType(mainClass, fieldName, (String) this.getOperands().get(1).getOperand());
			}
		} else {
			finalOperand = (String) this.getOperands().get(0).getOperand();
		}

		switch (this.getOperator()) {
		case Any:
			break;
		case Between:
			if (operandCount == 2) {
				criterion = Restrictions.between(fieldName, finalOperand, finalOperand2);
			}
			break;
		case Equals:
			criterion = Restrictions.eq(fieldName, finalOperand);
			break;
		case Greater:
			criterion = Restrictions.gt(fieldName, finalOperand);
			break;
		case GreaterEq:
			criterion = Restrictions.ge(fieldName, finalOperand);
			break;
		case In:
			Object[] values = new Object[operandCount];
			for (int o = 0; o < operandCount; o++) {
				values[o] = finalOperand;
			}
			criterion = Restrictions.in(fieldName, values);
			break;
		case IsNull:
			criterion = Restrictions.isNull(fieldName);
			break;
		case Like:
			criterion = Restrictions.like(fieldName, finalOperand);
			break;
		case ILike:
			//TODO remove the ternary operator
			// introduce exclude fields in client
			criterion = (finalOperand != null) ? Restrictions.ilike(fieldName, finalOperand) : null;
			break;
		case NotEquals:
			criterion = Restrictions.ne(fieldName, finalOperand);
			break;
		case NotLike:
			criterion = Restrictions.not(Restrictions.like(fieldName, finalOperand));
			break;
		case NotNull:
			criterion = Restrictions.isNotNull(fieldName);
			break;
		case Smaller:
			criterion = Restrictions.lt(fieldName, finalOperand);
			break;
		case SmallerEq:
			criterion = Restrictions.le(fieldName, finalOperand);
			break;
		case Statement:
			criterion = Restrictions.sqlRestriction((String)finalOperand);
			break;
		case DWithin:
			criterion = SpatialRestrictions.within(fieldName, (Geometry)finalOperand);
			break;
		}
		return criterion;
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
			
			if (field.getName().equals(this.getFieldName())) {
				
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
			}

		}

		return finalOperand;
	}
	
	/**
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
