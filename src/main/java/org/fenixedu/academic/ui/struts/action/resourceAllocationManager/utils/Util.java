/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.dto.InfoBuilding;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.resourceAllocationManager.ReadBuildings;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.SpaceClassification;

public class Util {

    public static List<LabelValueBean> readExistingBuldings(String name, String value) throws FenixServiceException {
        List<LabelValueBean> edificios = new ArrayList<LabelValueBean>();

        if (name != null) {
            edificios.add(new LabelValueBean(name, value));
        }

        final List<InfoBuilding> infoBuildings = ReadBuildings.run();
        Collections.sort(infoBuildings, new BeanComparator("name"));

        for (InfoBuilding infoBuilding : infoBuildings) {
            edificios.add(new LabelValueBean(infoBuilding.getName(), infoBuilding.getName()));
        }

        return edificios;
    }

    public static List<LabelValueBean> readTypesOfRooms(String name, String value) {
        List<LabelValueBean> tipos = new ArrayList<LabelValueBean>();

        if (name != null) {
            tipos.add(new LabelValueBean(name, value));
        }

        Collection<SpaceClassification> roomClassifications = Bennu.getInstance().getRootClassificationSet();
        for (SpaceClassification classification : SpaceUtils.sortByRoomClassificationAndCode(roomClassifications)) {
            if (classification.getParent() != null) {
                tipos.add(new LabelValueBean(classification.getAbsoluteCode() + " - " + classification.getName().getContent(),
                        classification.getExternalId().toString()));
            }
        }

        return tipos;
    }
}