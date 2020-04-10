package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc
	 *            Scanner from which a polynomial is to be read
	 * @throws IOException
	 *             If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	private static boolean ContainsTerm(Node poly, int deg) {
		boolean k = false;
		boolean q = true;
		Node poly1t = poly;
		while (!k && poly1t.next != null) {
			if (q = true) {
				q = false;
			} else {
				poly1t = poly1t.next;
			}
			if (poly1t.term.degree == deg) {
				k = true;
			}
		}
		return k;
	}

	// Return coefficient assumes the term exists in the linked list
	// lists should be checked before hand by ContainsTerm to ensure previous
	// statement is true
	private static float ReturnCoef(Node poly, int deg) {
		float i = 0;
		boolean k = false;
		boolean q = true;
		while (!k && poly.next != null) {
			if (q = true) {
				q = false;
			} else {
				poly = poly.next;
			}
			if (poly.term.degree == deg) {
				k = true;
				i = poly.term.coeff;
			}
		}
		return i;
	}

	private static int HighestDegree(Node poly) {
		int i = 0;
		boolean q = true;
		while (poly.next != null) {
			if (q = true) {
				i = poly.term.degree;
				q = false;
			} else
				poly = poly.next;
			if (poly.term.degree > i)
				i = poly.term.degree;
		}
		return i;
	}

	private static int LowestDegree(Node poly) {
		int i = 0;
		boolean q = true;
		while (poly.next != null) {
			if (q = true) {
				q = false;
				i = poly.term.degree;
			} else {
				poly = poly.next;
			}
			if (poly.term.degree < i)
				i = poly.term.degree;
		}
		return i;
	}

	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1
	 *            First input polynomial (front of polynomial linked list)
	 * @param poly2
	 *            Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	private static void addLast(Node poly, Node add) {
		if(poly == null) {
			System.out.println("hit2");
			poly = add;
			System.out.println(toString(poly));
		}
		else if(poly.next == null) {
			poly.next = add;
		}
		else {
			addLast(poly.next,add);
			System.out.println("hit3");
		}
	}
	
	public static Node add(Node poly1, Node poly2) {
		Node poly1t = poly1;
		Node poly2t = poly2;
		Node combined = null;
		while (poly1t != null && poly2t != null) {
			if (poly1t.term.degree < poly2t.term.degree) {
				System.out.println("hita");
				//addLast(combined,new Node(poly1t.term.coeff,poly1t.term.degree,null));
				combined = new Node(poly1t.term.coeff,poly1t.term.degree,combined);
				System.out.println(poly1t.toString());
				System.out.println(toString(combined));
				poly1t = poly1t.next;
			} else if (poly1t.term.degree > poly2t.term.degree) {
				System.out.println("hitb");
				//addLast(combined,new Node(poly2t.term.coeff,poly2t.term.degree,null));
				combined = new Node(poly2t.term.coeff,poly2t.term.degree,combined);
				poly2t = poly2t.next;
			} else {
				System.out.println("hitc");
				if(poly1t.term.coeff+poly2t.term.coeff!=0)
				combined = new Node(poly1t.term.coeff + poly2t.term.coeff,poly1t.term.degree,combined);
				poly1t = poly1t.next;
				poly2t = poly2t.next;
			}
		}
		if(poly1t!= null)
			while(poly1t!=null) {
			//addLast(combined,new Node(poly1t.term.coeff,poly1t.term.degree,null));
			combined = new Node(poly1t.term.coeff,poly1t.term.degree,combined);
			poly1t = poly1t.next;
			}
		else if(poly2t!=null) {
			while(poly2t!=null) {
				//addLast(combined,new Node(poly2t.term.coeff,poly2t.term.degree,null));
				combined = new Node(poly2t.term.coeff,poly2t.term.degree,combined);
				poly2t = poly2t.next;
				}
		}
		Node a = null;
		while(combined!= null) {
			a = new Node(combined.term.coeff,combined.term.degree,a);
			combined = combined.next;
		}
		return a;
		//System.out.println("hello"+combined.term.degree);
		//return combined;
	}

	public static void addSorted(Node poly,Node extra) {
		if(ContainsTerm(poly,extra.term.degree)) {
			if(poly.term.degree == extra.term.degree) {
				if(poly.term.coeff + extra.term.coeff == 0)
					poly = poly.next;
				else {
					poly.term.coeff += extra.term.coeff;
				}
			}
			else if(poly.next.term.degree == extra.term.degree) {
				if(poly.next.term.coeff + extra.term.coeff == 0) {
					poly.next = poly.next.next;
				}
				else {
					poly.next.term.coeff += extra.term.coeff;
				}
			}
			else {
				addSorted(poly.next,extra);
			}
		}
		else {
			if(poly == null) {
				poly = extra;
			}
			else if(extra.term.degree<poly.term.degree) {
				extra.next = poly;
				poly = extra;
			}
			else if(poly.next == null) {
				poly.next = extra;
			}
			else if(extra.term.degree<poly.next.term.degree) {
				extra.next = poly.next;
				poly.next = extra;
			}
			else {
				addSorted(poly.next,extra);
			}
		}
	}
	
	private static Node multiplyNodes(Node p1, Node p2) {
		float co = p1.term.coeff*p2.term.coeff;
		int deg = p1.term.degree+p2.term.degree;
		Node a = new Node(co,deg,null);
		return a;
	}
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1
	 *            First input polynomial (front of polynomial linked list)
	 * @param poly2
	 *            Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
//	public static Node multiply(Node poly1, Node poly2) {
//		Node combined = null;
//		Node poly1t = poly1;
//		Node poly2t = poly2;
//		while(poly1t!=null) {
//			poly2t = poly2;
//			while(poly2t != null) {
//				addSorted(combined,multiplyNodes(poly1t,poly2t));
//				poly2t=poly2t.next;
//			}
//			poly1t = poly1t.next;
//		}
//		return combined;
//	}
	public static Node multiply(Node poly1, Node poly2) {
		Node combined = null;
		Node poly1t = poly1;
		Node poly2t = poly2;
		float coef = 0;
		int deg = 0;
		while(poly1t!=null) {
			poly2t = poly2;
			while(poly2t != null) {
				coef = poly1t.term.coeff*poly2t.term.coeff;
				deg = poly1t.term.degree+poly2t.term.degree;
				combined = new Node(coef,deg,combined);
				poly2t=poly2t.next;
				System.out.println("hit");
			}
			poly1t = poly1t.next;
		}
//		boolean k = false;
//		boolean z = false;
//		while(!z) {
//			z = true;
//			Node a = combined;
//			Node b = combined.next;
//			while(b.next!=null) {
//				System.out.println("hit2");
//				if(b.term.degree<a.term.degree) {
//					a.next = b.next; 
//					b.next = a;
//					z = false;
//				}
//				else if(b.next.term.degree<b.term.degree) {
//					a.next = b.next;
//					a.next.next = b;
//					z = false;
//				}
//				else if(b.term.degree == b.next.term.degree) {
//					if(b.term.coeff+b.next.term.coeff !=0) {
//						b.term.coeff+=b.next.term.coeff;
//						b.next = b.next.next;
//						z = false;
//					}
//				}
//				a = a.next;
//				b = b.next;
//				
//			}
//			
//		}
		return combined;
		
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly
	 *            Polynomial (front of linked list) to be evaluated
	 * @param x
	 *            Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float count = 0;
		Node polyt = poly;
		while(polyt!=null) {
			count+=(Math.pow(x,polyt.term.degree))*polyt.term.coeff;
			polyt = polyt.next;
		}
		return count;
	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly
	 *            Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
