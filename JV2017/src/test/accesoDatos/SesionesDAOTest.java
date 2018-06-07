/** 
 *  Proyecto: Juego de la vida.
 *  Clase JUnit 4 para pruebas del DAO de sesiones y la parte de la fachada de Datos correspondiente.
 *  @since: prototipo2.1
 *  @source: SesionesDAOTest.java 
 *  @version: 2.1 - 2018/05/17 
 *  @author: ajp
 */

package test.accesoDatos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import accesoDatos.Datos;
import accesoDatos.DatosException;
import modelo.ModeloException;
import modelo.SesionUsuario;
import modelo.SesionUsuario.EstadoSesion;
import util.Fecha;

public class SesionesDAOTest {

	private static Datos fachada;
	private SesionUsuario sesionPrueba;

	/**
	 * Método que se ejecuta una sola vez al principio del conjunto pruebas.
	 * @throws DatosException 
	 */
	@BeforeClass
	public static void crearFachadaDatos() {
		fachada = new Datos();
	}

	/**
	 * Método que se ejecuta antes de cada @test.
	 * @throws ModeloException 
	 * @throws DatosException 
	 */
	@Before
	public void crearDatosPrueba() {
		try {
			sesionPrueba = new SesionUsuario(fachada.obtenerUsuario("III1R"), new Fecha(), EstadoSesion.EN_PREPARACION);
		} 
		catch (DatosException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Método que se ejecuta al terminar de cada @test.
	 */
	@After
	public void borraDatosPrueba() {
		fachada.borrarTodasSesiones();
		sesionPrueba = null;
	}

	@Test
	public void testObtenerSesionId() {
		try {
			fachada.altaSesion(sesionPrueba);
			// Busca la misma sesion almacenada.
			assertSame(sesionPrueba, fachada.obtenerSesion(sesionPrueba.getIdSesion()));
		} 
		catch (DatosException e) { 
		}
	}

	@Test
	public void testObtenerSesion() {
		try {
			fachada.altaSesion(sesionPrueba);
			// Busca la misma sesion almacenada.
			assertSame(sesionPrueba, fachada.obtenerSesion(sesionPrueba));
		} 
		catch (DatosException e) { 
		}
	}

	@Test
	public void testAltaSesion() {
		try {
			fachada.altaSesion(sesionPrueba);
			assertSame(sesionPrueba, fachada.obtenerSesion(sesionPrueba));
		} 
		catch (DatosException e) { 
		}
	}

	@Test
	public void testBajaSesionUsuario() {
		try {
			fachada.altaSesion(sesionPrueba);
			// Baja de la misma sesion almacenada.
			assertSame(sesionPrueba, fachada.bajaSesion(sesionPrueba.getIdSesion()));
		} 
		catch (DatosException e) { 
		}
	}

	@Test
	public void testActualizarSesion() {
		try {
			fachada.altaSesion(sesionPrueba);
			sesionPrueba.setEstado(EstadoSesion.CERRADA);
			fachada.actualizarSesion(sesionPrueba);
			assertEquals(fachada.obtenerSesion(sesionPrueba).getEstado(), EstadoSesion.CERRADA);
		} 
		catch (DatosException e) { 
		}
	}

	@Test
	public void testToStringDatosSesiones() {
		try {
			fachada.altaSesion(sesionPrueba);
			assertNotNull(fachada.toStringDatosSesiones());
		}
		catch (DatosException e) {
		}
	}

} //class
