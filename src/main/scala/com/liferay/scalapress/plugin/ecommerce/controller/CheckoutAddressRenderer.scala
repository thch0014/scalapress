package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{Address, DeliveryOption}
import org.springframework.validation.Errors
import xml.Elem

/** @author Stephen Samuel */
object CheckoutAddressRenderer {

    def renderDeliveryOptions(options: List[DeliveryOption]): Seq[Elem] =
        options.map(d => {
            val price = " £%1.2f".format(d.chargeIncVat / 100.0)
            <label class="radio">
                <input type="radio" name="deliveryOptionId" id="deliveryOptionId" value={d.id.toString}/>{d.name}{price}
            </label>
        })

    def renderDeliveryAddress(address: Address, options: List[DeliveryOption], errors: Errors) =
        <div class="checkout-delivery-input">
            <form method="POST" class="form-horizontal">
                <legend>Delivery Address</legend>

                <div class="control-group">
                    <label class="control-label" for="name">Name</label>
                    <div class="controls">
                        <input type="text" name="name" class="input-xlarge" placeholder="Name" value={address.name}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="company">Company</label>
                    <div class="controls">
                        <input type="text" name="company" class="input-xlarge" placeholder="Company" value={address
                      .company}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("company")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="address1">Address 1</label>
                    <div class="controls">
                        <input type="text" name="address1" class="input-xlarge" placeholder="Name" value={address
                      .address1}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("address1")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="address2">Address 2</label>
                    <div class="controls">
                        <input type="text" name="address2" class="input-xlarge" placeholder="Name" value={address
                      .address2}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("address2")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="town">City</label>
                    <div class="controls">
                        <input type="text" name="town" class="input-small" placeholder="City" value={address.town}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("town")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="name">Postcode</label>
                    <div class="controls">
                        <input type="text" name="postcode" class="input-xlarge" placeholder="Postcode" value={address
                      .postcode}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("postcode")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="country">Country</label>
                    <div class="controls">
                        <select name="country" class="input-large">
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
                </div>


                <div class="control-group">
                    <label class="control-label" for="telephone">Telephone</label>
                    <div class="controls">
                        <input type="text" name="telephone" class="input-small" placeholder="Telephone" value={address
                      .telephone}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("telephone")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="instructions">Delivery Instructions</label>
                    <div class="controls">
                        <textarea name="instructions" class="input-xxlarge">
                            {address.instructions}
                        </textarea>
                    </div>
                </div>

                <legend>Delivery Option</legend>

                <div class="control-group">
                    <label>
                        Delivery Option
                    </label>{renderDeliveryOptions(options)}
                </div>


                <button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>

}
