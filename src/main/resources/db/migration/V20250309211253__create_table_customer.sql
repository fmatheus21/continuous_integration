CREATE TABLE customer (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(70) NOT NULL,
  document VARCHAR(20) NOT NULL,
  UNIQUE (document)
);

INSERT INTO customer (id, name, document) VALUES
(1, 'Julia Gabrielly Carvalho', '42830860101'),
(2, 'Francisco Benício Levi Silva', '17783442838'),
(3, 'Eduardo Caleb Duarte', '25765679595'),
(4, 'Sara Bruna Agatha Baptista', '30190707321'),
(5, 'Erick Anthony José Porto', '29734231120'),
(6, 'Raimunda Nicole Nunes', '83190920605'),
(7, 'Diogo Levi Teixeira', '08559001301'),
(8, 'Luciana Beatriz Rezende', '96110258822'),
(9, 'Theo Emanuel Gabriel Fogaça', '30331349043'),
(10, 'Murilo Samuel Leonardo Rocha', '66124107791'),
(11, 'Thales Thales Danilo Santos', '53336245508'),
(12, 'Gabriel Juan Ian da Mata', '94086011492');