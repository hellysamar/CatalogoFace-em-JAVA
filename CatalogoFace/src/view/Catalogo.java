package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.DAO;
import utils.Validador;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Catalogo extends JFrame {

	// INSTANCIANDO OBJETOS JDBC
	DAO dao = new DAO();
	private Connection conn;
	private PreparedStatement pst;
	private ResultSet rs;
	
	// INSTANCIAR OBJETOS PARA OFLUXO DE BYTES
	private FileInputStream fis;
	
	// VARIÁVEL GLOBAL PARA ARMAZENAR O TAMANHO DA IMAGEM(Bytes)
	private int tamanho;
	
	// VARIAVEL PARA SABER SE A FOTO FOI ALTERADA OU NÃO
	private boolean fotoCarregada = false;
	

	private static final long serialVersionUID = 1L;
	private JPanel pnl;
	private JLabel lblStatus;
	private JLabel lblData;
	private JLabel lblRegistro;
	private JTextField txtRegistro;
	private JTextField txtNome;
	private JTextField txtEndereco;
	private JLabel lblFoto;
	private JList listaNomes;
	private JScrollPane scrollPaneNomes;
	private JButton btnPesquisar;
	private JButton btnCarregarFoto;
	private JButton btnAdicionar;
	private JButton btnEditar;
	private JButton btnDeletar;
	private JButton btnLimpar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Catalogo frame = new Catalogo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Catalogo() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				statusConn();
				setData();
			}
		});
		setTitle("Catálogo");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Catalogo.class.getResource("/icons/CF.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 392);
		pnl = new JPanel();
		pnl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				scrollPaneNomes.setVisible(false);
			}
		});
		pnl.setBackground(new Color(240, 240, 240));
		pnl.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(pnl);
		pnl.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(128, 128, 255));
		panel.setBounds(0, 293, 742, 60);
		pnl.add(panel);
		panel.setLayout(null);
		
		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOff.png")));
		lblStatus.setBounds(579, 0, 67, 60);
		panel.add(lblStatus);
		
		lblData = new JLabel("");
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblData.setBounds(20, 11, 455, 38);
		panel.add(lblData);
		
		lblRegistro = new JLabel("Número de Registro:");
		lblRegistro.setBackground(new Color(240, 240, 240));
		lblRegistro.setForeground(new Color(128, 128, 255));
		lblRegistro.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRegistro.setBounds(10, 26, 144, 17);
		pnl.add(lblRegistro);
		
		txtRegistro = new JTextField();
		txtRegistro.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtRegistro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtRegistro.setBounds(165, 26, 134, 20);
		pnl.add(txtRegistro);
		txtRegistro.setColumns(10);
		txtRegistro.setDocument(new Validador(6));
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBackground(new Color(240, 240, 240));
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNome.setForeground(new Color(128, 128, 255));
		lblNome.setBounds(10, 68, 46, 14);
		pnl.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listarNomesBanco();
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					scrollPaneNomes.setVisible(false);
					
					int confirma = JOptionPane.showConfirmDialog(null, "Usuário não Cadastrado! \nDeseja cadastrar o usuário " + txtNome.getText() + "?", "Aviso", JOptionPane.YES_NO_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRegistro.setText(null);
						txtRegistro.setEditable(false);
						btnPesquisar.setEnabled(false);
						txtEndereco.setText(null);
						btnAdicionar.setEnabled(true);
						btnCarregarFoto.setEnabled(true);
					} else {
						resetCampos();
					}
				}
			}
		});
		txtNome.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtNome.setBounds(10, 93, 289, 20);
		pnl.add(txtNome);
		txtNome.setColumns(10);
		txtNome.setDocument(new Validador(30));
		
		btnCarregarFoto = new JButton("Carregar foto");
		btnCarregarFoto.setEnabled(false);
		btnCarregarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		btnCarregarFoto.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnCarregarFoto.setForeground(new Color(128, 128, 255));
		btnCarregarFoto.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCarregarFoto.setBounds(479, 259, 115, 23);
		pnl.add(btnCarregarFoto);
		
		btnAdicionar = new JButton("");
		btnAdicionar.setEnabled(false);
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnAdicionar.setToolTipText("Adicionar");
		btnAdicionar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnAdicionar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF check.png")));
		btnAdicionar.setBounds(10, 192, 90, 90);
		pnl.add(btnAdicionar);
		
		btnEditar = new JButton("");
		btnEditar.setEnabled(false);
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editar();
			}
		});
		btnEditar.setToolTipText("Editar");
		btnEditar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnEditar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF edit.png")));
		btnEditar.setBounds(110, 192, 90, 90);
		pnl.add(btnEditar);
		
		btnDeletar = new JButton("");
		btnDeletar.setEnabled(false);
		btnDeletar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});
		btnDeletar.setToolTipText("Deletar");
		btnDeletar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnDeletar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF delete.png")));
		btnDeletar.setBounds(210, 192, 90, 90);
		pnl.add(btnDeletar);
		
		btnLimpar = new JButton("");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCampos();
			}
		});
		btnLimpar.setToolTipText("Limpar campos");
		btnLimpar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF change.png")));
		btnLimpar.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		btnLimpar.setBounds(310, 192, 90, 90);
		pnl.add(btnLimpar);
		
		scrollPaneNomes = new JScrollPane();
		scrollPaneNomes.setVisible(false);
		scrollPaneNomes.setBorder(null);
		scrollPaneNomes.setBounds(10, 111, 289, 82);
		pnl.add(scrollPaneNomes);
		
		listaNomes = new JList();
		listaNomes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				preencherPorNomeBanco();
			}
		});
		listaNomes.setBorder(null);
		scrollPaneNomes.setViewportView(listaNomes);
		
		JLabel lblEndereco = new JLabel("Endereço:");
		lblEndereco.setBackground(new Color(240, 240, 240));
		lblEndereco.setForeground(new Color(128, 128, 255));
		lblEndereco.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEndereco.setBounds(10, 124, 78, 14);
		pnl.add(lblEndereco);
		
		txtEndereco = new JTextField();
		txtEndereco.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtEndereco.setBounds(10, 149, 390, 20);
		pnl.add(txtEndereco);
		txtEndereco.setColumns(10);
		txtEndereco.setDocument(new Validador(30));
		
		lblFoto = new JLabel("New label");
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF photo.png")));
		lblFoto.setBounds(410, 11, 229, 242);
		pnl.add(lblFoto);
		
		btnPesquisar = new JButton("");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preencherPorRegistro();
			}
		});
		
		btnPesquisar.setToolTipText("Pesquisar");
		btnPesquisar.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF search.png")));
		btnPesquisar.setBounds(310, 23, 90, 90);
		pnl.add(btnPesquisar);
	}
	
	// Método para verificar o Status de conexão
	private void statusConn() {
		try {
			conn = dao.conectar();
			
			if (conn == null) {
//				System.out.println("Sem conexão");
				lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOff.png")));
			} else {
//				System.out.println("Conectado!");
				lblStatus.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/dbOn.png")));
			}
			
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void setData() {
		Date data = new Date();
		
		DateFormat formatar = DateFormat.getDateInstance(DateFormat.FULL);
		
		lblData.setText(formatar.format(data));
	}
	
	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecione a foto do usuário para o catálogo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagem(*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));
		int resultado = jfc.showOpenDialog(this);
		
		if (resultado == JFileChooser.APPROVE_OPTION) {
			try {
				 fis = new FileInputStream(jfc.getSelectedFile());
				 tamanho = (int) jfc.getSelectedFile().length();
				 Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
				 lblFoto.setIcon(new ImageIcon(foto));
				 lblFoto.updateUI();
				 
				 fotoCarregada = true;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void adicionar() {
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Nome!");
			txtNome.requestFocus();
			
		} else if (txtEndereco.getText().isEmpty()) {
			JOptionPane.showInternalMessageDialog(null, "Preencha o campo Endereço!");
			txtEndereco.requestFocus();
		} else if (tamanho == 0) {
			JOptionPane.showMessageDialog(null, "Selecione uma foto!");
		} else {
			
			String inserir = "INSERT INTO tblUsuarios (nome, foto, endereco) VALUES (?, ?, ?)"; // CREATE do CRUD
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(inserir);
				pst.setString(1, txtNome.getText());
				pst.setBlob(2, fis, tamanho);
				pst.setString(3, txtEndereco.getText());
				
				int confirma = pst.executeUpdate();
				
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
					resetCampos();
					
				} else {
					JOptionPane.showMessageDialog(null, "Erro! Usuário não cadastrado!");
				}
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void editar() {
		String sqlUpdate = "";
		
		if (fotoCarregada) {
			sqlUpdate = "UPDATE tblUsuarios SET nome = ?, foto = ?, endereco = ? WHERE registro = ?";
		} else {
			sqlUpdate = "UPDATE tblUsuarios SET nome = ?, endereco = ? WHERE registro = ?";
		}
		
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Nome!");
			txtNome.requestFocus();			
		} else if (txtEndereco.getText().isEmpty()) {
			JOptionPane.showInternalMessageDialog(null, "Preencha o campo Endereço!");
			txtEndereco.requestFocus();
		} else {
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(sqlUpdate);
				if (fotoCarregada) {
					pst.setString(1, txtNome.getText());
					pst.setBlob(2, fis, tamanho);
					pst.setString(3, txtEndereco.getText());
					pst.setString(4, txtRegistro.getText());
				} else {
					pst.setString(1, txtNome.getText());
					pst.setString(2, txtEndereco.getText());
					pst.setString(3, txtRegistro.getText());
				}
							
				int editou = pst.executeUpdate();
				
				if (editou == 1) {
					JOptionPane.showInternalMessageDialog(null, "Edição realizada com sucesso!");
					fotoCarregada = false;
				} else {
					JOptionPane.showInternalMessageDialog(null, "Edição não realizada!");
				}
				
				conn.close();
				
			} catch (Exception e) {
				System.out.println("ocorreu uma excessão ao tentar editar o usuário");
			}
		}
	}
	
	private void excluir() {
		String sqlExcluir = "DELETE FROM tblUsuarios WHERE registro = ?";
		
		int confirmaExcluir = JOptionPane.showConfirmDialog(null, "Confirma a Exclusão?", "Atenção", JOptionPane.YES_NO_OPTION);
		
		if (confirmaExcluir == 0) { // ou (confirmaExcluir == JOptionPane.YES_OPTION)
//			EXCLUIR
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(sqlExcluir);
				pst.setString(1, txtRegistro.getText());
				int excluiu = pst.executeUpdate();
				
				if (excluiu == 1) {
					JOptionPane.showMessageDialog(null, "Usuário excluido com sucesso!");
					resetCampos();
				}
				conn.close();
				
			} catch (Exception e) {
				System.out.println("Erro ao tentar excluir usuário!");
			}
		} else {
			
		}
	}
	
	private void listarNomesBanco() {
		// criando um vetor dinamico
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listaNomes.setModel(modelo);
		String LerLista = "SELECT * FROM tblUsuarios WHERE nome LIKE '" + txtNome.getText() + "%' ORDER BY nome;";
		
		try {
			conn = dao.conectar();
			pst = conn.prepareStatement(LerLista);
			rs = pst.executeQuery();
			
			while(rs.next()) {
				scrollPaneNomes.setVisible(true);
				modelo.addElement(rs.getString(2));
				
				if(txtNome.getText().isEmpty()) {
					scrollPaneNomes.setVisible(false);
				}
			}
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
//		scrollPaneNomes.setVisible(true);
	}
	
	private void preencherPorNomeBanco() {
		int linha = listaNomes.getSelectedIndex();
		if (linha >= 0) {
			String sql = "SELECT * FROM tblUsuarios WHERE nome LIKE '" + txtNome.getText() + "%' ORDER BY nome LIMIT " + (linha) + ", 1";
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				
				while (rs.next()) {
					scrollPaneNomes.setVisible(false);
					txtRegistro.setText(rs.getString(1));
					txtNome.setText(rs.getString(2));
					txtEndereco.setText(rs.getString(4));
					
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
					
					lblFoto.setIcon(foto);
					
					txtRegistro.setEditable(false);
					btnEditar.setEnabled(true);
					btnDeletar.setEnabled(true);
					btnCarregarFoto.setEnabled(true);
				}
			} catch (Exception e) {
				 System.out.println(e);
			}
		} else {
			scrollPaneNomes.setVisible(false);
		}
	}
	
	private void preencherPorRegistro() {
		if (txtRegistro.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Registro para buscar");
			txtRegistro.requestFocus();
		} else {
			String buscarR = "SELECT * FROM tblUsuarios WHERE registro = ?";
			
			try {
				conn = dao.conectar();
				pst = conn.prepareStatement(buscarR);
				pst.setString(1, txtRegistro.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					txtNome.setText(rs.getString(2));
					txtEndereco.setText(rs.getString(4));
					
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception e) {
						System.out.println(e);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH));
					
					lblFoto.setIcon(foto);
					
					txtRegistro.setEditable(false);
					btnCarregarFoto.setEnabled(true);
					btnEditar.setEnabled(true);
					btnDeletar.setEnabled(true);
			
				} else {
					int confirma = JOptionPane.showConfirmDialog(null, "Usuário não encontrado! \nDeseja iniciar novo Cadastro?", "Aviso", JOptionPane.YES_NO_OPTION);
					if (confirma == JOptionPane.YES_OPTION) {
						txtRegistro.setText(null);
						txtRegistro.setEditable(false);
						btnPesquisar.setEnabled(false);
						txtNome.setText(null);
						txtEndereco.setText(null);
						txtNome.requestFocus();
						btnAdicionar.setEnabled(true);
						btnCarregarFoto.setEnabled(true);
					} else {
						resetCampos();
					}
				}
				conn.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}
		
	}
	
	private void resetCampos() {
		txtRegistro.setText(null);
		txtNome.setText(null);
		txtEndereco.setText(null);
		lblFoto.setIcon(new ImageIcon(Catalogo.class.getResource("/icons/CF photo.png")));
		txtNome.requestFocus();
		scrollPaneNomes.setVisible(false);
		fotoCarregada = false;
		tamanho = 0;
						
		txtRegistro.setEditable(true);
		btnPesquisar.setEnabled(true);
		btnAdicionar.setEnabled(false);
		btnCarregarFoto.setEnabled(false);
	}
}
