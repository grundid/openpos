--    Openbravo POS is a point of sales application designed for touch screens.
--    Copyright (C) 2010 Openbravo, S.L.
--    http://sourceforge.net/projects/openbravopos
--
--    This file is part of Openbravo POS.
--
--    Openbravo POS is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    Openbravo POS is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

-- Database upgrade script for MYSQL

-- v2.30.2 - v2.40.0

CREATE TABLE IF NOT EXISTS `TIMERECORDING` (
  `TIMERECORDING_ID` INT NOT NULL AUTO_INCREMENT ,
  `EMPLOYEE_NAME` VARCHAR(255) NOT NULL ,
  `WORK_DATE` DATE NOT NULL ,
  `COMMING_TIME` INT NOT NULL ,
  `LEAVING_TIME` INT NULL ,
  `PAUSE_DURATION` INT NULL ,
  `WORKING_TIME` INT NULL ,
  `EARNINGS` INT NULL ,
  `CREATED` TIMESTAMP NULL ,
  PRIMARY KEY (`TIMERECORDING_ID`) ,
  INDEX `IDX_TIMERECORDING_EMPLOYEE_NAME_WORK_DATE` (`EMPLOYEE_NAME` ASC, `WORK_DATE` ASC) )
ENGINE = INNODB;

DELETE FROM SHAREDTICKETS;

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};