package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import main.DatabaseJDBC;

public class UserDao {
	private static final Logger logger = Logger.getLogger(UserDao.class.getName());
	
	public Optional<Integer> getUser(String login) {
		Connection connection = DatabaseJDBC.getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT id from user WHERE login = ? ;");
			ps.setString(1, login);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return Optional.of(rs.getInt("id"));
			}
			
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) { }
		}
		
		return Optional.empty();
	}
	
	public List<String> getAll() {
		Connection connection = DatabaseJDBC.getConnection();
		List<String> users = new ArrayList<>();
		
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT login from user ;");
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				users.add(rs.getString("login"));
			}
			
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) { }
		}
		
		return users;
	}
	
	public Optional<Integer> addUser(String login) {
		Connection connection = DatabaseJDBC.getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO user (login) VALUES (?) ;",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, login);
			
			int rowCount = ps.executeUpdate();
			
			if (rowCount > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return Optional.of(rs.getInt(1));
				}
			}
			
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) { }
		}
		
		return Optional.empty();
	}
	
	public void delete() {
		Connection connection = DatabaseJDBC.getConnection();
		
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM user ;");
			
			int rowCount = ps.executeUpdate();
			logger.info("Deleted " + rowCount + " rows");
			
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception ignored) { }
		}
	}
	
	public void fill() {
		Connection connection = DatabaseJDBC.getConnection();
		names.forEach(s -> {
			try {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO user (login) VALUES (?) ;",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, s);
				
				int rowCount = ps.executeUpdate();
				
				if (rowCount > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
	//					return Optional.of(rs.getInt(1));
					}
				}
				
			} catch (SQLException e) {
				logger.severe(e.getMessage());
			} 
		});
		
		try {
			connection.close();
		} catch (Exception ignored) { }
	}
	
	private static List<String> names = new ArrayList<>();
	static {
		//String str = "Ada, Adela, Adelajda, Adrianna, Agata, Agnieszka, Aldona, Aleksandra, Alicja, Alina, Amanda, Amelia, Anastazja, Andżelika, Aneta, Anita, Anna, Antonina, Adam, Adolf, Adrian, Albert, Aleksander, Aleksy, Alfred, Amadeusz, Andrzej, Antoni, Arkadiusz, Arnold, Artur, Barbara, Beata, Berenika, Bernadeta, Blanka, Bogusława, Bożena, Bartłomiej, Bartosz, Benedykt, Beniamin, Bernard, Błażej, Bogdan, Bogumił, Bogusław, Bolesław, Borys, Bronisław, Cecylia, Celina, Czesława, Cezary, Cyprian, Cyryl, Czesław, Dagmara, Danuta, Daria, Diana, Dominika, Dorota, Damian, Daniel, Dariusz, Dawid, Dionizy, Dominik, Donald, Edyta, Eliza, Elwira, Elżbieta, Emilia, Eugenia, Ewa, Ewelina, Edward, Emanuel, Emil, Eryk, Eugeniusz, Felicja, Franciszka, Fabian, Feliks, Ferdynand, Filip, Franciszek, Fryderyk, Gabriela, Grażyna, Gabriel, Gerard, Grzegorz, Gustaw, Halina, Hanna, Helena, Henryk, Herbert, Hilary, Hubert, Iga, Ilona, Irena, Irmina, Iwona, Izabela, Ignacy, Igor, Ireneusz, Jadwiga, Janina, Joanna, Jolanta, Jowita, Judyta, Julia, Julita, Justyna, Jacek, Jakub, Jan, Janusz, Jarosław, Jerzy, Joachim, Józef, Julian, Juliusz, Kamila, Karina, Karolina, Katarzyna, Kazimiera, Kinga, Klaudia, Kleopatra, Kornelia, Krystyna, Kacper, Kajetan, Kamil, Karol, Kazimierz, Klaudiusz, Konrad, Krystian, Krzysztof, Laura, Lena, Leokadia, Lidia, Liliana, Lucyna, Ludmiła, Luiza, Lech, Leon, Leszek, Lucjan, Ludwik, Magdalena, Maja, Malwina, Małgorzata, Marcelina, Maria, Marianna, Mariola, Marlena, Marta, Martyna, Marzanna, Marzena, Matylda, Melania, Michalina, Milena, Mirosława, Monika, Maciej, Maksymilian, Marceli, Marcin, Marek, Marian, Mariusz, Mateusz, Michał, Mieczysław, Mikołaj, Miłosz, Mirosław, Nadia, Natalia, Natasza, Nikola, Nina, Nikodem, Norbert, Olaf, Olgierd, Oskar, Patryk, Paweł, Piotr, Przemysław, Radosław, Rafał, Remigiusz, Robert, Roman, Rudolf, Ryszard, Pamela, Patrycja, Paula, Paulina, Sebastian, Seweryn, Sławomir, Stanisław, Stefan, Sylwester, Szymon, Sabina, Sandra, Sara, Sonia, Stanisława, Stefania, Stella, Sylwia, Wacław, Waldemar, Wiesław, Wiktor, Witold, Władysław, Włodzimierz, Wojciech";
		String str = "Ada, Adela, Adelajda, Adrianna, Agata, Agnieszka, Aldona, Aleksandra, Alicja, Alina, Amanda, Amelia, Anastazja, Andzelika, Aneta, Anita, Anna, Antonina, Adam, Adolf, Adrian, Albert, Aleksander, Aleksy, Alfred, Amadeusz, Andrzej, Antoni, Arkadiusz, Arnold, Artur, Barbara, Beata, Berenika, Bernadeta, Blanka, Boguslawa, Bozena, Bartlomiej, Bartosz, Benedykt, Beniamin, Bernard, Blazej, Bogdan, Bogumil, Boguslaw, Boleslaw, Borys, Bronislaw, Cecylia, Celina, Czeslawa, Cezary, Cyprian, Cyryl, Czeslaw, Dagmara, Danuta, Daria, Diana, Dominika, Dorota, Damian, Daniel, Dariusz, Dawid, Dionizy, Dominik, Donald, Edyta, Eliza, Elwira, Elzbieta, Emilia, Eugenia, Ewa, Ewelina, Edward, Emanuel, Emil, Eryk, Eugeniusz, Felicja, Franciszka, Fabian, Feliks, Ferdynand, Filip, Franciszek, Fryderyk, Gabriela, Grazyna, Gabriel, Gerard, Grzegorz, Gustaw, Halina, Hanna, Helena, Henryk, Herbert, Hilary, Hubert, Iga, Ilona, Irena, Irmina, Iwona, Izabela, Ignacy, Igor, Ireneusz, Jadwiga, Janina, Joanna, Jolanta, Jowita, Judyta, Julia, Julita, Justyna, Jacek, Jakub, Jan, Janusz, Jaroslaw, Jerzy, Joachim, Józef, Julian, Juliusz, Kamila, Karina, Karolina, Katarzyna, Kazimiera, Kinga, Klaudia, Kleopatra, Kornelia, Krystyna, Kacper, Kajetan, Kamil, Karol, Kazimierz, Klaudiusz, Konrad, Krystian, Krzysztof, Laura, Lena, Leokadia, Lidia, Liliana, Lucyna, Ludmila, Luiza, Lech, Leon, Leszek, Lucjan, Ludwik, Magdalena, Maja, Malwina, Malgorzata, Marcelina, Maria, Marianna, Mariola, Marlena, Marta, Martyna, Marzanna, Marzena, Matylda, Melania, Michalina, Milena, Miroslawa, Monika, Maciej, Maksymilian, Marceli, Marcin, Marek, Marian, Mariusz, Mateusz, Michal, Mieczyslaw, Mikolaj, Milosz, Miroslaw, Nadia, Natalia, Natasza, Nikola, Nina, Nikodem, Norbert, Olaf, Olgierd, Oskar, Patryk, Pawel, Piotr, Przemyslaw, Radoslaw, Rafal, Remigiusz, Robert, Roman, Rudolf, Ryszard, Pamela, Patrycja, Paula, Paulina, Sebastian, Seweryn, Slawomir, Stanislaw, Stefan, Sylwester, Szymon, Sabina, Sandra, Sara, Sonia, Stanislawa, Stefania, Stella, Sylwia, Waclaw, Waldemar, Wieslaw, Wiktor, Witold, Wladyslaw, Wlodzimierz, Wojciech";
		String[] split = str.split(", ");
		names = Arrays.asList(split);
	}
}
