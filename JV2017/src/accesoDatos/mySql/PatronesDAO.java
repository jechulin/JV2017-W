/** 
 * Proyecto: Juego de la vida.
 * Resuelve todos los aspectos del almacenamiento del DTO Patron.
 * Utiliza base de datos mySQL
 * Colabora en el patron Fachada.
 * @since: prototipo2.2
 * @source: PatronesDAO.java 
 * @version: 2.2 - 2018.07.07
 * @author: alejandro
 */

package accesoDatos.mySql;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import modelo.Mundo;
import modelo.Patron;

public class PatronesDAO implements OperacionesDAO {

	// Singleton 
	private static PatronesDAO instancia = null;

	// Elemento de almacenamiento, base datos db4o
	private ObjectContainer db;

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static PatronesDAO getInstancia() {
		if (instancia == null) {
			instancia = new PatronesDAO();
		}
		return instancia;
	}

	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private PatronesDAO() {
		//db = Conexion.getDB();
		try {
			obtener("Demo0");
		} 
		catch (DatosException e) {
			cargarPredeterminados();
		}
	}

	/**
	 *  Método para generar datos predeterminados.
	 */
	private void cargarPredeterminados() {
		byte[][] esquemaDemo =  new byte[][]{ 
			{ 0, 0, 0, 0 }, 
			{ 1, 0, 1, 0 }, 
			{ 0, 0, 0, 1 }, 
			{ 0, 1, 1, 1 }, 
			{ 0, 0, 0, 0 }
		};

		try {
			Patron patronDemo = new Patron("Demo0", esquemaDemo);
			alta(patronDemo);
		} 
		catch (DatosException e) { 
			e.printStackTrace();
		}
	}

	/**
	 *  Cierra conexión.
	 */
	@Override
	public void cerrar() {
		db.close();
	}

	//OPERACIONES DAO
	/**
	 * Obtiene un Patron dado su identificador.
	 * @param nombre - el nombre del Patron a recuperar.
	 * @return - el Patron encontrado.
	 * @throws DatosException - si no existe. 
	 */	
	@Override
	public Patron obtener(String nombre) throws DatosException {
		ObjectSet<Patron>  result;
		Query consulta = db.query();
		consulta.constrain(Patron.class);
		consulta.descend("nombre").constrain(nombre).equal();
		result = consulta.execute();
		if (result.size()  > 0) {
			return result.get(0);
		}	
		else {
			throw new DatosException("Obtener: "+ nombre + " no existe.");
		}	
	}

	/**
	 * Búsqueda de Patron dado un objeto, reenvía al método que utiliza nombre.
	 * @param obj - el Patron a buscar.
	 * @return - el Patron encontrado.
	 * @throws DatosException - si no existe. 
	 */
	@Override
	public Patron obtener(Object obj) throws DatosException  {
		return this.obtener(((Patron) obj).getNombre());
	}

	/**
	 * Obtiene todos los objetos Patron almacenados.
	 * @return - la List con todos los encontrados.
	 */
	@Override
	public List<Patron> obtenerTodos() {
		Query consulta = db.query();
		consulta.constrain(Patron.class);
		return consulta.execute();
	}

	/**
	 * Alta de un nuevo Patron sin repeticiones según el campo nombre. 
	 * @param obj - Patron a almacenar.
	 * @throws DatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		Patron patron = (Patron) obj;
		try { 
			obtener(patron.getNombre());
		}
		catch (DatosException e) {
			db.store(patron);
			return;
		}
		throw new DatosException("Alta: " + patron.getNombre() + " ya existe.");
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param nombre - el nombre del Patron a eliminar.
	 * @return - el Patron eliminado.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Patron baja(String nombrePatron) throws DatosException {
		assert nombrePatron != null;
		assert nombrePatron != "";
		assert nombrePatron != " ";
		Patron patron = null;
		try {
			patron = obtener(nombrePatron);
			db.delete(patron);
			return patron;
		}
		catch (DatosException e) {
			throw new DatosException("Baja: " + nombrePatron + " no existe.");
		}
	}

	/**
	 *  Actualiza datos de un Patron reemplazando el almacenado por el recibido.
	 *	@param obj - Patron con las modificaciones.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException {
		assert obj != null;
		Patron patron = (Patron) obj;
		Patron patronAux = null;
		try {
			patronAux = obtener(patron.getNombre());
			patronAux.setNombre(patron.getNombre());
			patronAux.setEsquema(patron.getEsquema());
			db.store(patronAux);
		} 
		catch (DatosException e) {
			throw new DatosException("Actualizar: " + patron.getNombre() + " no existe.");
		}
	}


	/**
	 * Obtiene el listado de todos los objetos Patron almacenados.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		ObjectSet<Patron>  result;
		Query consulta = db.query();
		consulta.constrain(Patron.class);
		result = consulta.execute();
		for (Patron patron: result) {
			listado.append("\n" + patron);
		}
		return listado.toString();
	}

	/**
	 * Obtiene el listado de todos los identificadores de patron almacenados.
	 * @return el texto con el volcado de datos.
	 */
	public String listarId() {
		StringBuilder listado = new StringBuilder();
		ObjectSet<Patron>  result;
		Query consulta = db.query();	
		consulta.constrain(Patron.class);
		result = consulta.execute();
		for (Patron patron: result) {
			if (patron != null) {
				listado.append(patron.getNombre() + "\n");
			}
		}
		return listado.toString();
	} 

	/**
	 * Quita todos los objetos Patron de la base de datos.
	 */
	@Override
	public void borrarTodo() {
		// Elimina cada uno de los objetos obtenidos.
		for (Patron patron: obtenerTodos()) {
			db.delete(patron);
		}
		cargarPredeterminados();
	}

} //class
