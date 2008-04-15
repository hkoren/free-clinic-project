<?
	function getMeasurementType($type)
	{
		if (is_numeric($type)) {
			$query = "select * from measurementType where _id = $type";
			$result = mysql_query($query);
			if (mysql_num_rows($result) == 0) { // error
				trigger_error("Measurement type '$type' is invalid.");
			}
			$row = mysql_fetch_assoc($result);
		} else { // lookup in database
			$query = "select * from measurementType where name = '$type'";
			$result = mysql_query($query);
			if (mysql_num_rows($result) == 0) { // error
				trigger_error("Measurement type '$type' is invalid.");
			}
			$row = mysql_fetch_assoc($result);
		}
		return $row;
	}

	function addMeasurement($type, $patientID, $value) {
		global $userID;
		$measurementType = getMeasurementType($type);
		$measurementTypeID = $measurementType["_id"];
		if ($value =="") return false;
		else
		{
			$query = "insert into measurements(patientID, userID, timestamp, value, measurementTypeID) values ($patientID, $userID, CURRENT_TIMESTAMP(), $value, $measurementTypeID)";
			mysql_query($query);
			return true;		
		}
	}

	
	function getLastMeasurement($type, $patientID) {
		global $userID;
		$measurementType = getMeasurementType($type);
		$measurementTypeID = $measurementType["_id"];
		$query = "select * from measurements where _id in " . 
				 "(select max(_id) as _id from measurements where patientID=$patientID and userID=$userID and measurementTypeID=$measurementTypeID" .
				 " group by patientID,userID,measurementTypeID);";
		$result = mysql_query($query);
		if (mysql_num_rows($result) > 0)
		{
			$row = mysql_fetch_assoc($result);
			return $row;
		}
		else return '';		
	}


	function showPatientMeasurements($patientID)
	{
		$query="select m.*, mt.name as typeName from measurements m " .
				"join measurementType mt on m.measurementTypeID=mt._id " .
				"where patientID = $patientID";
		$measurements=mysql_query($query) or trigger_error(mysql_error());
		showMeasurements($measurements);
	}
	
	function showMeasurements($measurements)
	{
		$count = mysql_num_rows($measurements);
		if ($count==0) echo "<p>none found</p>";
		else
		{
			echo "<table id=\"sortable_measurement\" class=\"datagrid\"><thead>" .
				"<tr><th mochi:format=\"str\">ID</th>".
				"<th mochi:format=\"str\">Name</th>".
				"<th mochi:format=\"str\">Date</th>" .
				"<th mochi:format=\"str\">Value</th></tr>" .
				"</thead><tbody>";
			while ($measurement=mysql_fetch_assoc($measurements))
			{
				echo "<tr><td>${measurement['_id']}</td>";
				echo "<td>${measurement['typeName']}</td>" .
						"<Td>${measurement['timestamp']}</td>" .
						"<td>${measurement['value']}</td></tr>";
			}
			echo "</tbody></table>";
		}
	}
		function showMeasurementTypes($measurementTypes)
	{
		$count = mysql_num_rows($measurementTypes);
		if ($count==0) echo "<p>none found</p>";
		else
		{
			echo "<table id=\"sortable_measurement\" class=\"datagrid\"><thead>" .
				"<tr><th mochi:format=\"str\">ID</th>".
				"<th mochi:format=\"str\">Name</th>".
				"<th mochi:format=\"str\">Label</th>" .
				"<th mochi:format=\"str\">Unit</th>" .
				"<th mochi:format=\"str\">Size</th></tr>" .
				"</thead><tbody>";
			while ($measurementType=mysql_fetch_assoc($measurementTypes))
			{
				$measurementTypeID = $measurementType['_id'];
				echo "<tr onclick=\"window.location.href='measurementType.php?measurementTypeID=$measurementTypeID'\"><td>$measurementTypeID</td>";
				echo "<td>{$measurementType['name']}</a></td>" .
						"<Td>{$measurementType['label']}</td>" .
						"<td>{$measurementType['unit']}</td>" .
						"<td>{$measurementType['size']}</td></tr>";
			}
			echo "</tbody></table>";
		}
	}
	function enterMeasurement($type, $patientID)
	{
		$measurement = getMeasurementType($type);
		$name = $measurement['name'];
		$unit = $measurement['unit'];
		
			
		echo "<table><tr><td><label for=\"$name\">{$measurement['label']}</label></td>" .
			"<td><input name=\"$name\" type=\"text\" class=\"indentField\" id=\"$name\" size=\"${measurement['size']}\"/></td></tr>";
		
		$last = getLastMeasurement($type, $patientID);
		if ($last != '') {
			$timestamp = $last['timestamp'];
			$date = substr($timestamp,0,10);
			$value = $last['value'];
			echo "<tr><td align=\"right\">$date</td><td align=\"right\"><strong>$value $unit</strong></td></tr>";
		}
		echo "</table>";
	}
?>

