# CREACION DE LA BASE DE DATOS
create database bancoxd;
use bancoxd;

# CREACION DE TABLAS DE LA BASE DE DATOS
create table cliente (id int auto_increment primary key, cedula varchar(15) unique not null, nombres varchar(50) not null, apellidos varchar(50) not null, fecha_nac date not null, telefono varchar(20) not null, email varchar(255) unique not null, direccion varchar(255) not null, fecha_registro datetime not null default current_timestamp, estado enum('activo', 'inactivo') not null default 'activo');

create table tipo_cuenta (id int auto_increment primary key, nombre varchar(30) unique not null);
insert into tipo_cuenta(nombre) values('Ahorros'), ('Corriente');

create table tipo_movimiento (id int auto_increment primary key, nombre varchar(30) unique not null);
insert into tipo_movimiento(nombre) values('Depósito'), ('Retiro'), ('Transferencia');

create table cuenta (id int auto_increment primary key, id_cliente int not null, id_tipo_cuenta int not null, numero varchar(20) unique not null, saldo_ini decimal(12,2) not null default 0.00, saldo_actual decimal(12,2) not null default 0.00, fecha_crea datetime not null default current_timestamp, estado enum('Activa', 'Inactiva', 'Bloqueada') not null default 'Activa', clave varchar(4) not null, constraint fk_cuenta_cliente foreign key(id_cliente) references cliente(id), constraint fk_tipo_cuenta foreign key(id_tipo_cuenta) references tipo_cuenta(id));

create table movimiento (id int auto_increment primary key, id_cuenta int not null, id_tipo_movimiento int not null, id_cuenta_destino int null, monto decimal(12, 2) not null, saldo_antes decimal(12, 2) not null, saldo_despues decimal(12, 2) not null, fecha_movimiento datetime not null default current_timestamp, descripcion varchar(300) null, constraint fk_movimiento_cuenta foreign key(id_cuenta) references cuenta(id), constraint fk_movimiento_tipo foreign key(id_tipo_movimiento) references tipo_movimiento(id), constraint fk_movimiento_cuenta_destino foreign key(id_cuenta_destino) references cuenta(id));

create table empleado(id int auto_increment primary key, usuario varchar(15) unique not null, contrasenia varchar(30) not null, nombres varchar(50) not null, apellidos varchar(50) not null);

# DATOS DE EMPLEADO DE SERVICIO AL CLIENTE
insert into empleado(usuario, contrasenia, nombres, apellidos) values('fsf7', 'n1N1n1', 'Felipe', 'Figueroa');
insert into empleado(usuario, contrasenia, nombres, apellidos) values('ace7', '1234', 'Allison', 'Canteral');

# DATOS DE CLIENTES
insert into cliente(cedula, nombres, apellidos, fecha_nac, telefono, email, direccion) values('0999999999', 'Felipe Santiago', 'Figueroa Cevallos', '1993-09-30', '0988888888', 'fsfigueroa77@yopmail.com', 'norte de gye');
insert into cliente(cedula, nombres, apellidos, fecha_nac, telefono, email, direccion) values('0988888888', 'Maria Fernanda', 'Perez Paez', '2000-01-01', '0901010101', 'maferpp@yopmail.com', 'ceibos');

# DATOS CUENTAS DE CLIENTES
insert into cuenta(id_cliente, id_tipo_cuenta, numero, clave) value(1, 1, '1012345678', '7777');
insert into cuenta(id_cliente, id_tipo_cuenta, numero, clave) value(2, 1, '1087654321', '1234');
