<? global $name, $class, $patients ?>
<div id=section_<?= $name ?> class="<?= $class ?>">
<div class=section_header_options><span class=handle_move>Move</span> &nbsp; 
<a href=enroll.php?patientID=<?= $patientID ?>>Edit</a>
</div>
<div class=section_header onclick="Element.toggle('section_<?= $name ?>_content');">Patient Info</div>
<div id=section_<?= $name ?>_content class=section_content>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="150" class="label">Name </td>
        <td><?= $patients['lastName'] ?>, <?= $patients['firstName'] ?> <?= $patients['middleName'] ?></td>
      </tr>
       <tr>
        <td class="label">Gender</td>
        <td><?=$patients['genderName']?></td>
      </tr>
      <tr>
        <td class="label">date Of Birth</td>
        <td><?=$patients['dateOfBirth'] ?></td>
      </tr>
      <tr>
        <td class="label">SSN</td>
        <td><? if ($patients['SSN'] != 0) echo substr($patients['SSN'],0,3).'-'.substr($patients['SSN'],3,2).'-'.substr($patients['SSN'],5,4) ?></td>
      </tr>
      <tr>
        <td class="label">Address</td>
        <td><?=$patients['address1'] ?></td>
      </tr>
      <tr>
        <td class="label"></td>
        <td><?=$patients['address2'] ?></td>
      </tr>
      <tr>
        <td class="label">City</td>
        <td><?=$patients['city'] ?></td>
      </tr>
      <tr>
        <td class="label">State</td>
        <td><?=$patients['state'] ?></td>
      </tr>
      <tr>
        <td class="label">Zip</td>
        <td><?=$patients['zip'] ?></td>
      </tr>
      <tr>
        <td class="label">Phone Number</td>
        <td><?=$patients['phoneNumber'] ?></td>
      </tr>
      <tr>
        <td class="label">Emergency Contact</td>
        <td><?=$patients['emergencyName'] ?></td>
      </tr>
      <tr>
        <td class="label">Relationship</td>
        <td><?=$patients['emergencyRelationship'] ?></td>
      </tr>
      <tr>
        <td class="label">Emergency Contact Number</td>
        <td><?=$patients['emergencyPhoneNumber'] ?></td>
      </tr>
      <tr>
        <td class="label">Registration Date </td>
        <td><?= date("M j, Y", strtotime($patients['createDate'])) ?></td>
      </tr>
    </table>
</div>	
</div>	
