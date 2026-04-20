from afip import Afip

afip = Afip({ "access_token": "TU_ACCESS_TOKEN" })

# Aqui deben cambiar los datos por los que correspondan. 
# Esta request de ejemplo incluye todos posibles 
# valores para ejecutar la automatizacion auth-web-service-prod, 
# puede que algun valor sea opcional.
data = {
    "cuit": "20111111112",
    "username": "20111111112",
    "password": "contraseña#segura?",
    "alias": "afipsdk",
    "service": "wsfe"
}

try:
    # Ejecutamos la automatizacion
    response = afip.createAutomation("auth-web-service-prod", data, True)

    # Mostramos la respuesta por pantalla
    print(response)
except Exception as error:
    # En caso de error lo mostramos por pantalla
	print(error)