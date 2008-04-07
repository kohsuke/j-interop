/**j-Interop (Pure Java implementation of DCOM protocol)  
 * Copyright (C) 2006  Vikram Roopchand
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * Though a sincere effort has been made to deliver a professional, 
 * quality product,the library itself is distributed WITHOUT ANY WARRANTY; 
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jinterop.dcom.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**<p>Forms the definition of a Java Interface. Please note that Overloads not allowed.  
 * Primarily used to form a definition for callback. </p>
 * 
 * <br><i>Please refer to <b>MSInternetExplorer</b> example for more details on how to use this class.</i><br>
 * 
 * @since 1.0
 *
 */
public final class JIInterfaceDefinition implements Serializable
{
	private static final long serialVersionUID = 7683984211902254797L;
	private String interfaceIdentifier = null;
    private Map opnumVsMethodInfo = new HashMap();
    private Map dispIdVsMethodInfo = new HashMap();
    private Map nameVsMethodInfo = new HashMap();
    private int nextNum = 0;
    Object instance = null;
    Class clazz = null;
    private boolean dispInterface = true; 
    
    /**Creates an Interface definition. By default, the "dispinterface" property is true.
     * 
     * @param interfaceIdentifier
     */
	public JIInterfaceDefinition(String interfaceIdentifier)
	{
		this.interfaceIdentifier = interfaceIdentifier;
	}
	
	 /**Creates an Interface definition. Set <code>isDispInterface</code> interface to false if this interface does not
	 * support IDispatch based calls.
     * 
     * @param interfaceIdentifier
     * @param isDispInterface true if IDispatch is supported ("dispinterface"), false otherwise
     */
	public JIInterfaceDefinition(String interfaceIdentifier, boolean isDispInterface)
	{
		this.interfaceIdentifier = interfaceIdentifier;
		this.dispInterface = isDispInterface;
	}
	
	/**Adds a Method Descriptor. <b>Methods should be added in the same order as they appear in the IDL</b>.
	 * 
	 * <p> Please note that overloaded Methods are not allowed.
	 * 
	 * @param methodDescriptor
	 */
	public void addMethodDescriptor(JIMethodDescriptor methodDescriptor)
	{
		if (nameVsMethodInfo.containsKey(methodDescriptor.getMethodName()))
		{
			throw new IllegalArgumentException(JISystem.getLocalizedMessage(JIErrorCodes.JI_CALLBACK_OVERLOADS_NOTALLOWED));
		}

		methodDescriptor.setMethodNum(nextNum);
		nextNum++;
		
		opnumVsMethodInfo.put(new Integer(methodDescriptor.getMethodNum()),methodDescriptor);
		if (dispInterface)
		{
			if (methodDescriptor.getMethodDispID() == -1)
			{
				throw new IllegalArgumentException(JISystem.getLocalizedMessage(JIErrorCodes.JI_METHODDESC_DISPID_MISSING));
			}
			dispIdVsMethodInfo.put(new Integer(methodDescriptor.getMethodDispID()),methodDescriptor);	
		}
		
		nameVsMethodInfo.put(methodDescriptor.getMethodName(),methodDescriptor);
		
		
	}
	
	/**Returns the descriptor identified by it's Method number. <br>
	 * 
	 * @param opnum
	 * @return
	 */
	public JIMethodDescriptor getMethodDescriptor(int opnum)
	{
		return (JIMethodDescriptor)opnumVsMethodInfo.get(new Integer(opnum));
	}

	/**Returns the descriptor identified by it's dispId. <br>
	 * 
	 * @param dispId
	 * @return
	 */
	public JIMethodDescriptor getMethodDescriptorForDispId(int dispId)
	{
		return (JIMethodDescriptor)dispIdVsMethodInfo.get(new Integer(dispId));
	}

	
	/**Returns the descriptor identified by it's Method Name. <br>
	 * 
	 * @param name
	 * @return
	 */
	public JIMethodDescriptor getMethodDescriptor(String name)
	{
		return (JIMethodDescriptor)nameVsMethodInfo.get(name);
	}
	
	/**Returns all descriptors. <br>
	 * 
	 * @return
	 */
	public JIMethodDescriptor[] getMethodDescriptors()
	{
		return (JIMethodDescriptor[])opnumVsMethodInfo.values().toArray(new JIMethodDescriptor[opnumVsMethodInfo.values().size()]);
	}
	
	/**Returns the interface identifier of this definition. <br>
	 * 
	 * @return
	 */
	public String getInterfaceIdentifier()
	{
		return interfaceIdentifier;
	}
	
	/**Removes the descriptor identified by it's Method Num. <p>  
	 * Please note that removal of a sequential Method Num can have unpredictable results during a call. <br>  
	 * 
	 * @param opnum
	 */
	public void removeMethodDescriptor(int opnum)
	{
		JIMethodDescriptor methodDescriptor = (JIMethodDescriptor)opnumVsMethodInfo.remove(new Integer(opnum));
		if (methodDescriptor != null)
		{
			nameVsMethodInfo.remove(methodDescriptor.getMethodName());
		}
	}
	
	/**Removes the descriptor identified by it's Method Name. <p>  
	 * Please note that removal of a sequential Method Num can have unpredictable results during a call.  <br>
	 * 
	 * @param methodName
	 */
	public void removeMethodDescriptor(String methodName)
	{
		JIMethodDescriptor methodDescriptor = (JIMethodDescriptor)nameVsMethodInfo.remove(methodName);
		if (methodDescriptor != null)
		{
			nameVsMethodInfo.remove(new Integer(methodDescriptor.getMethodNum()));
		}
	}
	
	/**Returns whether this interface supports IDispatch or not.
	 * 
	 * @return true if IDispatch is supported.
	 */
	public boolean isDispInterface()
	{
		return dispInterface;
	}
}

