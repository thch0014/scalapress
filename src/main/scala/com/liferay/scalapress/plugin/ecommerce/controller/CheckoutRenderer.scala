package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption
import org.springframework.validation.Errors
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPlugin}

/** @author Stephen Samuel */
object CheckoutRenderer {

    def renderPaymentOptions(plugin: SagepayFormPlugin) = {

        val params = SagepayFormService.params(plugin).map(e => <input type="text" name={e._1} value={e._2}/>)

        <div class="container">
            <form method="POST" action={SagepayFormService.TestUrl}>
                {params}<button type="submit" class="btn btn-primary">Pay by sage pay</button>
            </form>
        </div>
    }

    def renderDeliveryOptions(options: List[DeliveryOption]) =
        options.map(d => <option value={d.id.toString}>
            {d.name + " GBP " + d.chargeIncVat}
        </option>)

    def renderDeliveryAddress(options: List[DeliveryOption], errors: Errors) =
        <div class="container">
            <form method="POST">
                <legend>Delivery Address</legend>
                <div class="control-group">
                    <label for="name">
                        Name
                    </label>
                    <input name="name" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="company">
                        Company
                    </label>
                    <input name="company" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("company")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="address1">
                        Address 1
                    </label>
                    <input name="address1" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("address1")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="address2">
                        Address 2
                    </label>
                    <input name="address2" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("address2")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="city">
                        City
                    </label>
                    <input name="city" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("city")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="postcode">
                        Postcode
                    </label>
                    <input name="postcode" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("postcode")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="country">
                        Country
                    </label>
                    <select name="country">
                        <option>United Kingdom</option>
                        <option>United States</option>
                        <option>Afghanistan</option>
                        <option>Albania</option>
                        <option>Algeria</option>
                        <option>Andorra</option>
                        <option>Angola</option>
                        <option>Antigua</option>
                        <option>Argentina</option>
                        <option>Armenia</option>
                        <option>Australia</option>
                        <option>Austria</option>
                        <option>Azerbaijan</option>
                        <option>Bahamas</option>
                        <option>Bahrain</option>
                        <option>Bangladesh</option>
                        <option>Barbados</option>
                        <option>Belarus</option>
                        <option>Belgium</option>
                        <option>Belize</option>
                        <option>Benin</option>
                        <option>Bhutan</option>
                        <option>Bolivia</option>
                        <option>Bosnia Herzegovina</option>
                        <option>Botswana</option>
                        <option>Brazil</option>
                        <option>Brunei</option>
                        <option>Bulgaria</option>
                        <option>Burkina</option>
                        <option>Burundi</option>
                        <option>Cambodia</option>
                        <option>Cameroon</option>
                        <option>Canada</option>
                        <option>Cape Verde</option>
                        <option>Central African Rep</option>
                        <option>Chad</option>
                        <option>Chile</option>
                        <option>China</option>
                        <option>Colombia</option>
                        <option>Comoros</option>
                        <option>Congo</option>
                        <option>Congo Democratic Rep</option>
                        <option>Costa Rica</option>
                        <option>Croatia</option>
                        <option>Cuba</option>
                        <option>Cyprus</option>
                        <option>Czech Republic</option>
                        <option>Denmark</option>
                        <option>Djibouti</option>
                        <option>Dominica</option>
                        <option>Dominican Republic</option>
                        <option>East Timor</option>
                        <option>Ecuador</option>
                        <option>Egypt</option>
                        <option>El Salvador</option>
                        <option>Equatorial Guinea</option>
                        <option>Eritrea</option>
                        <option>Estonia</option>
                        <option>Ethiopia</option>
                        <option>Fiji</option>
                        <option>Finland</option>
                        <option>France</option>
                        <option>Gabon</option>
                        <option>Gambia</option>
                        <option>Georgia</option>
                        <option>Germany</option>
                        <option>Ghana</option>
                        <option>Greece</option>
                        <option>Grenada</option>
                        <option>Guatemala</option>
                        <option>Guinea</option>
                        <option>Guinea-Bissau</option>
                        <option>Guyana</option>
                        <option>Haiti</option>
                        <option>Honduras</option>
                        <option>Hungary</option>
                        <option>Iceland</option>
                        <option>India</option>
                        <option>Indonesia</option>
                        <option>Iran</option>
                        <option>Iraq</option>
                        <option>Ireland</option>
                        <option>Israel</option>
                        <option>Italy</option>
                        <option>Ivory Coast</option>
                        <option>Jamaica</option>
                        <option>Japan</option>
                        <option>Jordan</option>
                        <option>Kazakhstan</option>
                        <option>Kenya</option>
                        <option>Kiribati</option>
                        <option>Korea North</option>
                        <option>Korea South</option>
                        <option>Kosovo</option>
                        <option>Kuwait</option>
                        <option>Kyrgyzstan</option>
                        <option>Laos</option>
                        <option>Latvia</option>
                        <option>Lebanon</option>
                        <option>Lesotho</option>
                        <option>Liberia</option>
                        <option>Libya</option>
                        <option>Liechtenstein</option>
                        <option>Lithuania</option>
                        <option>Luxembourg</option>
                        <option>Macedonia</option>
                        <option>Madagascar</option>
                        <option>Malawi</option>
                        <option>Malaysia</option>
                        <option>Maldives</option>
                        <option>Mali</option>
                        <option>Malta</option>
                        <option>Mauritania</option>
                        <option>Mauritius</option>
                        <option>Mexico</option>
                        <option>Micronesia</option>
                        <option>Moldova</option>
                        <option>Monaco</option>
                        <option>Mongolia</option>
                        <option>Montenegro</option>
                        <option>Morocco</option>
                        <option>Mozambique</option>
                        <option>Namibia</option>
                        <option>Nauru</option>
                        <option>Nepal</option>
                        <option>Netherlands</option>
                        <option>New Zealand</option>
                        <option>Nicaragua</option>
                        <option>Niger</option>
                        <option>Nigeria</option>
                        <option>Norway</option>
                        <option>Oman</option>
                        <option>Pakistan</option>
                        <option>Palau</option>
                        <option>Panama</option>
                        <option>Papua New Guinea</option>
                        <option>Paraguay</option>
                        <option>Peru</option>
                        <option>Philippines</option>
                        <option>Poland</option>
                        <option>Portugal</option>
                        <option>Qatar</option>
                        <option>Romania</option>
                        <option>Russian Federation</option>
                        <option>Rwanda</option>
                        <option>St Kitts
                            &amp;
                            Nevis
                        </option>
                        <option>St Lucia</option>
                        <option>Saint Vincent
                            &amp;
                            the Grenadines
                        </option>
                        <option>Samoa</option>
                        <option>San Marino</option>
                        <option>Sao Tome
                            &amp;
                            Principe
                        </option>
                        <option>Saudi Arabia</option>
                        <option>Senegal</option>
                        <option>Serbia</option>
                        <option>Seychelles</option>
                        <option>Sierra Leone</option>
                        <option>Singapore</option>
                        <option>Slovakia</option>
                        <option>Slovenia</option>
                        <option>Solomon Islands</option>
                        <option>Somalia</option>
                        <option>South Africa</option>
                        <option>South Sudan</option>
                        <option>Spain</option>
                        <option>Sri Lanka</option>
                        <option>Sudan</option>
                        <option>Suriname</option>
                        <option>Swaziland</option>
                        <option>Sweden</option>
                        <option>Switzerland</option>
                        <option>Syria</option>
                        <option>Taiwan</option>
                        <option>Tajikistan</option>
                        <option>Tanzania</option>
                        <option>Thailand</option>
                        <option>Togo</option>
                        <option>Tonga</option>
                        <option>Trinidad Tobago</option>
                        <option>Tunisia</option>
                        <option>Turkey</option>
                        <option>Turkmenistan</option>
                        <option>Tuvalu</option>
                        <option>Uganda</option>
                        <option>Ukraine</option>
                        <option>United Arab Emirates</option>
                        <option>United Kingdom</option>
                        <option>United States</option>
                        <option>Uruguay</option>
                        <option>Uzbekistan</option>
                        <option>Vanuatu</option>
                        <option>Vatican City</option>
                        <option>Venezuela</option>
                        <option>Vietnam</option>
                        <option>Yemen</option>
                        <option>Zambia</option>
                        <option>Zimbabwe</option>
                    </select>
                </div>
                <div class="control-group">
                </div>
                <div class="control-group">
                    <label for="telephone">
                        Telephone
                    </label>
                    <input name="telephone" type=" "/>
                    <span class="help-inline">
                        {Option(errors.getFieldError("telephone")).map(_.getDefaultMessage).getOrElse("")}
                    </span>
                </div>
                <div class="control-group">
                    <label for="instructions">
                        Delivery Instructions
                    </label>
                    <textarea name="instructions"></textarea>
                </div>

                <legend>Delivery Address</legend>
                <div class="control-group">
                    <label for="instructions">
                        Delivery Option
                    </label>
                    <select name="country">
                        {renderDeliveryOptions(options)}
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>

}
